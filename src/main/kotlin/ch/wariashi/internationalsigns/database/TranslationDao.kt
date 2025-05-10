package ch.wariashi.internationalsigns.database

import ch.wariashi.internationalsigns.InternationalSigns
import java.sql.SQLException

/**
 * Saves information about translations in the database and retrieves them when needed.
 *
 * @author Wariashi
 */
class TranslationDao(plugin: InternationalSigns) : AbstractDao(plugin.configuration) {
    /**
     * The [InternationalSigns] logger.
     */
    private val logger = plugin.logger

    init {
        val createSql = """
            CREATE TABLE IF NOT EXISTS translation (
            sign_id INT NOT NULL,
            locale VARCHAR(255) NOT NULL,
            front1 VARCHAR(255) NOT NULL,
            front2 VARCHAR(255) NOT NULL,
            front3 VARCHAR(255) NOT NULL,
            front4 VARCHAR(255) NOT NULL,
            back1 VARCHAR(255) NOT NULL,
            back2 VARCHAR(255) NOT NULL,
            back3 VARCHAR(255) NOT NULL,
            back4 VARCHAR(255) NOT NULL,
            PRIMARY KEY (sign_id, locale),
            FOREIGN KEY (sign_id) REFERENCES sign(id)
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
