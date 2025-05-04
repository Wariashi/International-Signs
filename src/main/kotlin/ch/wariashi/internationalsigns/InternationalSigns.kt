package ch.wariashi.internationalsigns

import ch.wariashi.internationalsigns.database.SignDao
import org.bukkit.plugin.java.JavaPlugin

class InternationalSigns : JavaPlugin() {
    val configuration = Config(this)

    override fun onEnable() {
        SignDao(this)
    }
}
