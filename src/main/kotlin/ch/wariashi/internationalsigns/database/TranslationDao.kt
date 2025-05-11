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

    /**
     * Inserts or updates a translation.
     *
     * @param signId the ID of the sign in the database
     * @param locale the locale of the translation
     * @param front1 the first line on the front of the sign
     * @param front2 the second line on the front of the sign
     * @param front3 the third line on the front of the sign
     * @param front4 the fourth line on the front of the sign
     * @param back1 the first line on the back of the sign
     * @param back2 the second line on the back of the sign
     * @param back3 the third line on the back of the sign
     * @param back4 the fourth line on the back of the sign
     */
    fun insertOrUpdate(
        signId: Int,
        locale: String,
        front1: String,
        front2: String,
        front3: String,
        front4: String,
        back1: String,
        back2: String,
        back3: String,
        back4: String
    ) {
        // update the translation
        val updateSql = """
            UPDATE translation SET
                front1 = ?,
                front2 = ?,
                front3 = ?,
                front4 = ?,
                back1 = ?,
                back2 = ?,
                back3 = ?,
                back4 = ?
            WHERE sign_id = ? AND locale = ?
        """.trimIndent()
        try {
            val connection = createConnection()
            connection.use { connection ->
                val updateStatement = connection.prepareStatement(updateSql)
                updateStatement.use { updateStatement ->
                    updateStatement.setString(1, front1)
                    updateStatement.setString(2, front2)
                    updateStatement.setString(3, front3)
                    updateStatement.setString(4, front4)
                    updateStatement.setString(5, back1)
                    updateStatement.setString(6, back2)
                    updateStatement.setString(7, back3)
                    updateStatement.setString(8, back4)
                    updateStatement.setInt(9, signId)
                    updateStatement.setString(10, locale)
                    val affectedRows = updateStatement.executeUpdate()
                    if (0 < affectedRows) {
                        return
                    }
                }
            }
        } catch (exception: SQLException) {
            logger.severe(exception.message)
        }

        // insert the translation if nothing has been updated
        val insertSql = "INSERT INTO translation VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        try {
            val connection = createConnection()
            connection.use { connection ->
                val insertStatement = connection.prepareStatement(insertSql)
                insertStatement.use { insertStatement ->
                    insertStatement.setInt(1, signId)
                    insertStatement.setString(2, locale)
                    insertStatement.setString(3, front1)
                    insertStatement.setString(4, front2)
                    insertStatement.setString(5, front3)
                    insertStatement.setString(6, front4)
                    insertStatement.setString(7, back1)
                    insertStatement.setString(8, back2)
                    insertStatement.setString(9, back3)
                    insertStatement.setString(10, back4)
                    insertStatement.execute()
                }
            }
        } catch (exception: SQLException) {
            logger.severe(exception.message)
        }
    }
}
