package ch.wariashi.internationalsigns

import ch.wariashi.internationalsigns.database.SignDao
import ch.wariashi.internationalsigns.database.TranslationDao
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.data.type.HangingSign
import org.bukkit.block.data.type.Sign
import org.bukkit.block.data.type.WallHangingSign
import org.bukkit.block.data.type.WallSign
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
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
     * Deletes information of broken [signs][Sign] from the database.
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
     * Saves information of placed [signs][Sign] to the database.
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
