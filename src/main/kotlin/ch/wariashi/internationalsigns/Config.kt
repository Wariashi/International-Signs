package ch.wariashi.internationalsigns

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

/**
 * The [Config] contains several parameters that can be set by the server admin.
 *
 * @author Wariashi
 */
class Config(plugin: JavaPlugin) {
    /**
     * The [FileConfiguration] that is used to configure the [InternationalSigns] plugin.
     */
    private val fileConfiguration = plugin.config

    /**
     * The config key to configure the password that is used to access the database.
     */
    private val databasePassword = "database.password"

    /**
     * The config key to configure the URL that is used to access the database.
     */
    private val databaseUrl = "database.url"

    /**
     * The config key to configure the user that is used to access the database.
     */
    private val databaseUser = "database.user"

    init {
        val fileConfiguration = plugin.config

        fileConfiguration.addDefault(databasePassword, "password")
        fileConfiguration.addDefault(databaseUrl, "jdbc:mysql://localhost/international_signs")
        fileConfiguration.addDefault(databaseUser, "user")

        fileConfiguration.options().copyDefaults(true)
        plugin.saveConfig()
    }

    /**
     * Returns the URL that is used to access the database.
     * If no URL has been defined in the config file, an empty String is returned.
     *
     * @return the URL that is used to access the database
     */
    fun getDatabaseUrl(): String {
        return fileConfiguration.getString(databaseUrl) ?: ""
    }
}
