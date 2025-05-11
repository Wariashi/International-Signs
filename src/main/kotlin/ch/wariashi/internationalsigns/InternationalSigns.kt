package ch.wariashi.internationalsigns

import ch.wariashi.internationalsigns.database.SignDao
import ch.wariashi.internationalsigns.database.TranslationDao
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.data.type.HangingSign
import org.bukkit.block.data.type.Sign
import org.bukkit.block.data.type.WallHangingSign
import org.bukkit.block.data.type.WallSign
import org.bukkit.block.sign.Side
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.plugin.java.JavaPlugin

class InternationalSigns : JavaPlugin(), Listener {
    val configuration = Config(this)
    val signDao = SignDao(this)
    val translationDao = TranslationDao(this)

    override fun onEnable() {
        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(this, this)
    }

    /**
     * Deletes information about broken [signs][Sign] from the database.
     *
     * @param event the [Event] that is sent when a [Block] is broken.
     */
    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        val block = event.block
        if (isSign(block)) {
            val location = block.location
            signDao.delete(location)
        }
    }

    /**
     * Saves information about placed [signs][Sign] to the database.
     *
     * @param event the [Event] that is sent when a [Block] is placed.
     */
    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent) {
        val block = event.blockPlaced
        if (isSign(block)) {
            val location = block.location
            signDao.insert(location)
        }
    }

    /**
     * Updates a translation of a [Sign] when it has been changed.
     *
     * @param event the [Event] that is sent when a [Sign] is changed.
     */
    @EventHandler
    fun onSignChangeEvent(event: SignChangeEvent) {
        // find the sign ID
        val block = event.block
        val location = block.location
        var signId = signDao.findIdByLocation(location)

        // add the sign to the database if it does not exist yet
        if (signId == null) {
            signDao.insert(location)
            signId = signDao.findIdByLocation(location)
        }
        if (signId == null) {
            logger.warning("Sign at $location could not be added to the database.")
            return
        }

        // get previous values
        val sign = block.state as org.bukkit.block.Sign
        val front = sign.getSide(Side.FRONT)
        var front1 = front.getLine(0)
        var front2 = front.getLine(1)
        var front3 = front.getLine(2)
        var front4 = front.getLine(3)
        val back = sign.getSide(Side.BACK)
        var back1 = back.getLine(0)
        var back2 = back.getLine(1)
        var back3 = back.getLine(2)
        var back4 = back.getLine(3)

        // update values
        val side = event.side
        if (side == Side.FRONT) {
            front1 = event.getLine(0) ?: ""
            front2 = event.getLine(1) ?: ""
            front3 = event.getLine(2) ?: ""
            front4 = event.getLine(3) ?: ""
        } else {
            back1 = event.getLine(0) ?: ""
            back2 = event.getLine(1) ?: ""
            back3 = event.getLine(2) ?: ""
            back4 = event.getLine(3) ?: ""
        }

        // update the database
        val player = event.player
        val locale = player.locale
        translationDao.insertOrUpdate(
            signId = signId,
            locale = locale,
            front1 = front1,
            front2 = front2,
            front3 = front3,
            front4 = front4,
            back1 = back1,
            back2 = back2,
            back3 = back3,
            back4 = back4
        )
    }

    /**
     * Checks whether the [Block] is a sign.
     *
     * @param block the [Block] to check
     *
     * @return `true`, if the [Block] is a [HangingSign], [Sign], [WallHangingSign] or [WallSign], otherwise `false`
     */
    private fun isSign(block: Block): Boolean {
        val blockData = block.blockData
        return when (blockData) {
            is HangingSign -> true
            is Sign -> true
            is WallHangingSign -> true
            is WallSign -> true
            else -> false
        }
    }
}
