package ch.wariashi.internationalsigns

import ch.wariashi.internationalsigns.database.SignDao
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.data.type.Sign
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin

class InternationalSigns : JavaPlugin(), Listener {
    val configuration = Config(this)
    val signDao = SignDao(this)

    override fun onEnable() {
        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(this, this)
    }

    /**
     * Saves information of placed [signs][Sign] to the database.
     *
     * @param event the [Event] that is sent when a [Block] is placed.
     */
    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent) {
        val blockPlaced = event.blockPlaced
        val blockData = blockPlaced.blockData
        if (blockData is Sign) {
            val location = blockPlaced.location
            signDao.insert(location)
        }
    }
}
