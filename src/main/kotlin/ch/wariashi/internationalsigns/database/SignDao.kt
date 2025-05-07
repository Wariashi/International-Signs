package ch.wariashi.internationalsigns.database

import ch.wariashi.internationalsigns.InternationalSigns
import org.bukkit.Location
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

    /**
     * Deletes a sign entry from the database.
     *
     * @param location the location of the sign
     */
    fun delete(location: Location) {
        val world = location.world?.name
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        try {
            val connection = createConnection()
            connection.use { connection ->
                val deleteSql = "DELETE FROM sign WHERE world = ? AND x = ? AND y = ? AND z = ?"
                val deleteStatement = connection.prepareStatement(deleteSql)
                deleteStatement.use { deleteStatement ->
                    deleteStatement.setString(1, world)
                    deleteStatement.setInt(2, x)
                    deleteStatement.setInt(3, y)
                    deleteStatement.setInt(4, z)
                    deleteStatement.execute()
                }
            }
        } catch (exception: SQLException) {
            logger.severe(exception.message)
        }
    }

    /**
     * Inserts a new sign entry into the database if it does not exist yet.
     *
     * @param location the location of the sign
     */
    fun insert(location: Location) {
        val world = location.world?.name
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        try {
            val connection = createConnection()
            connection.use { connection ->
                // check if the sign already exists
                val selectSql = "SELECT 1 FROM sign WHERE world = ? AND x = ? AND y = ? AND z = ?"
                val selectStatement = connection.prepareStatement(selectSql)
                selectStatement.use { selectStatement ->
                    selectStatement.setString(1, world)
                    selectStatement.setInt(2, x)
                    selectStatement.setInt(3, y)
                    selectStatement.setInt(4, z)
                    val result = selectStatement.executeQuery()
                    if (result.next()) {
                        return
                    }
                }

                // insert sign
                val insertSql = "INSERT INTO sign (world, x, y, z) VALUES (?, ?, ?, ?)"
                val insertStatement = connection.prepareStatement(insertSql)
                insertStatement.use { insertStatement ->
                    insertStatement.setString(1, world)
                    insertStatement.setInt(2, x)
                    insertStatement.setInt(3, y)
                    insertStatement.setInt(4, z)
                    insertStatement.execute()
                }
            }
        } catch (exception: SQLException) {
            logger.severe(exception.message)
        }
    }
}
