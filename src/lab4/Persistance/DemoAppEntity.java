package lab4.Persistance;

import lab4.Impl.DemoAppImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DemoAppEntity {
    private final static Logger LOGGER = LoggerFactory.getLogger(DemoAppEntity.class);
    private static final String INSERT_QUERY = "INSERT INTO mock_table(app_id) values(?)";
    private static final String SELECT_QUERY = "SELECT app_id FROM mock_table WHERE id = ?";
    private static final String SELECT_SERIAL_QUERY = "SELECT app_id FROM mock_table2 WHERE id = ?";
    private final Connection conn;

    public DemoAppEntity() {
        this.conn = H2Init.getConnection();
    }

    public DemoAppImpl loadSerial(int id) {
        return loadFrom(id, SELECT_SERIAL_QUERY);
    }

    public int save(DemoAppImpl thing) {
        try (PreparedStatement statement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, thing.getId());
            statement.executeUpdate();

            LOGGER.info("Saving #" + thing.getId() + " to DB");
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DemoAppImpl load(int dbID) {
        return loadFrom(dbID, SELECT_QUERY);
    }

    private DemoAppImpl loadFrom(int dbID, String query) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, dbID);
            statement.execute();

            ResultSet resultId = statement.getResultSet();
            if(resultId.next()) {
                LOGGER.info("Loading #"+ resultId.getInt(1) +" from DB");
                return new DemoAppImpl(resultId.getInt(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
