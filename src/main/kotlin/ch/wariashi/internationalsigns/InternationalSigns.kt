package ch.wariashi.internationalsigns

import org.bukkit.plugin.java.JavaPlugin

class InternationalSigns : JavaPlugin() {

    override fun onEnable() {
        logger.info("Enabled")
    }

    override fun onDisable() {
        logger.info("Disabled")
    }
}
