package lab4.Persistance;

import java.sql.*;

public class H2Init {
    private static Connection connection;

    private static final String CREATE_TABLE =
        "CREATE TABLE mock_table (" +
            "id INT AUTO_INCREMENT," +
            "app_id INT NOT NULL" +
        ")"
    ;

    private static final String CREATE_MORE_USELESS_TABLE =
        "CREATE TABLE mock_table2 (" +
            "id INT," +
            "app_id INT NOT NULL" +
        ")"
    ;

    private static final String INSERT_MORE_USELESS =
        "INSERT INTO mock_table2 (id, app_id) values(?, ?)";

    public static void resetDatabase() {
        try {
            Connection conn = getConnection();
            dropObjects(conn);
            createTables(conn);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement createStatement = conn.createStatement();
        createStatement.execute(CREATE_TABLE);
        createStatement.close();
        createStatement = conn.createStatement();
        createStatement.execute(CREATE_MORE_USELESS_TABLE);
        createStatement.close();

        PreparedStatement insertStatement;
        for (int i = 0; i < 100; i++) {
            insertStatement = conn.prepareStatement(INSERT_MORE_USELESS);
            insertStatement.setInt(1, i);
            insertStatement.setInt(2, 9876000 + i);
            insertStatement.execute();
            insertStatement.close();
        }
    }

    private static void dropObjects(Connection conn) throws SQLException {
        Statement resetStatement = conn.createStatement();
        resetStatement.execute("DROP ALL OBJECTS DELETE FILES");
    }

    synchronized static Connection getConnection() {
        if(connection == null) {
            connection = establishNewConnection();
        }

        return connection;
    }

    private static Connection establishNewConnection() {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./my_db", "uer", "pass");
            conn.setAutoCommit(true);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            throw new AssertionError(e);
        }
    }
}
