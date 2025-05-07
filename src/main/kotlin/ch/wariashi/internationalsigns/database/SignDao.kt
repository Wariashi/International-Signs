package ch.wariashi.internationalsigns.database

import ch.wariashi.internationalsigns.InternationalSigns
import java.sql.SQLException

/**
 * Saves information about the location of signs in the database and retrieves them when needed.
 *
 * @author Wariashi
 */
class SignDao(plugin: InternationalSigns) : AbstractDao(plugin.configuration) {
    /**
     * The [InternationalSigns] logger.
     */
    private val logger = plugin.logger

    init {
        val createSql = """
            CREATE TABLE IF NOT EXISTS sign (
            id INT NOT NULL AUTO_INCREMENT,
            world VARCHAR(255) NOT NULL,
            x INT NOT NULL,
            y INT NOT NULL,
            z INT NOT NULL,
            PRIMARY KEY (id)
            )
        """.trimIndent()
        try {
            val connection = createConnection()
            connection.use { connection ->
                val statement = connection.prepareStatement(createSql)
                statement.use { statement ->
                    statement.execute()
                }
            }
        } catch (exception: SQLException) {
            logger.severe(exception.message)
        }
    }
}
