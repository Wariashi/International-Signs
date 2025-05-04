package ch.wariashi.internationalsigns

import org.bukkit.plugin.java.JavaPlugin

class InternationalSigns : JavaPlugin() {
    val configuration = Config(this)

    override fun onEnable() {
        logger.info("database URL: " + configuration.getDatabaseUrl())
    }
}
