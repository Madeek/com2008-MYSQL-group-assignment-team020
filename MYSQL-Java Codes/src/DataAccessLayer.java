import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the data access layer for the application.
 * It provides methods for connecting to and disconnecting from the database,
 * as well as executing queries and updates.
 *
 * Author: Emmanuel Ita
 */
public class DataAccessLayer {

    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020";
    private static final String DB_USER = "team020";
    private static final String DB_PASSWORD = "asheet1Ie";

    public void connect() throws SQLException, ClassNotFoundException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (SQLException | ClassNotFoundException e) {

            e.printStackTrace();
        }
    }

    public void disconnect() {

        // Disconnect from the database
        if (connection != null) {

            try {

                connection.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {

        return this.connection;
    }

    public ResultSet executeQuery(String query) {

        // Execute a query and return the result set
        ResultSet rs = null;

        // your code to execute the query and assign the result set to rs
        try (Statement stmt = connection.createStatement()) {

            rs = stmt.executeQuery(query);

        } catch (SQLException e) {

            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, e.getMessage());
        }

        return rs;
    }

    public void executeUpdate(String query) {

        // Execute an update
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(query);

        } catch (SQLException e) {

            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

}
