package hexlet.code.repository;

import hexlet.code.model.Url;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static hexlet.code.App.getDbUrl;

@Slf4j
public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_At) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, Timestamp.from(url.getCreatedAt()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                log.info("LOGGING: UrlRepository.save saving name: " + url.getName());
                log.info("LOGGING: UrlRepository.save saving id: " + url.getId());
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_At");
                var url = new Url(name);
                url.setCreatedAt(createdAt.toInstant());
                url.setId(id);
                log.info("LOGGING: UrlRepository.find Repo size: " + UrlRepository.getEntities().size());
                log.info("LOGGING: UrlRepository.find name: " + url.getName());
                log.info("LOGGING: UrlRepository.find id: " + url.getId());
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities()  {
        var sql = "SELECT * FROM urls ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_At");
                var url = new Url(name);
                url.setCreatedAt(createdAt.toInstant());
                url.setId(id);
                result.add(url);
                log.info("LOGGING: UrlRepository.getEntyties added name = " + url.getName());
                log.info("LOGGING: UrlRepository.getEntyties added id = " + url.getId());
            }
            return result;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public static Optional<Url> findName(String inputName) {
        var sql = "SELECT * FROM urls WHERE name = ? LIMIT 1";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_At");
                var url = new Url(name);
                url.setCreatedAt(createdAt.toInstant());
                return Optional.of(url);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() throws SQLException {
        try (var conn = dataSource.getConnection();
            var stmt = conn.createStatement()) {
            var dbUrl = getDbUrl();
            stmt.executeUpdate("DELETE FROM url_checks");
            stmt.executeUpdate("DELETE FROM urls");
            if (dbUrl.contains("h2")) {
                stmt.execute("ALTER TABLE url_checks ALTER COLUMN id RESTART WITH 1");
                stmt.execute("ALTER TABLE urls ALTER COLUMN id RESTART WITH 1");
            } else if (dbUrl.contains("postgresql")) {
                stmt.execute("ALTER SEQUENCE url_checks_id_seq RESTART WITH 1");
                stmt.execute("ALTER SEQUENCE urls_id_seq RESTART WITH 1");
            } else {
                System.out.println("Очистка таблиц БД не рассчитана на базу данных " + dbUrl);
            }
        }
    }
}
