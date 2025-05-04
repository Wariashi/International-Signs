package ch.wariashi.internationalsigns.database

import ch.wariashi.internationalsigns.Config
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * A superclass for classes that interact with the database.
 *
 * @author Wariashi
 */
abstract class AbstractDao(private val config: Config) {
    /**
     * Creates a [Connection] to the database.
     *
     * @return a new [Connection] to the database
     *
     * @throws SQLException if a database error occurs
     */
    fun createConnection(): Connection {
        val password = config.getDatabasePassword()
        val url = config.getDatabaseUrl()
        val user = config.getDatabaseUser()
        return DriverManager.getConnection(url, user, password)
    }
}
