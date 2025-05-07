package hexlet.code.repository;

import hexlet.code.model.Url;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
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
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
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
                url.setCreatedAt(createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities()  {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_At");
                log.info("ATT. CreatedAt is " + createdAt + " from DB urls (getEntitities)");
                var url = new Url(name);
                url.setCreatedAt(createdAt);
                url.setId(id);
                result.add(url);
            }
            return result;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public static void clear() throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            var dbUrl = getDbUrl();
            if (dbUrl.contains("h2")) {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                stmt.execute("TRUNCATE TABLE url_checks RESTART IDENTITY");
                stmt.execute("TRUNCATE TABLE urls RESTART IDENTITY");
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            } else  if (dbUrl.contains("postgresql")) {
                stmt.execute("TRUNCATE TABLE url_checks, urls RESTART IDENTITY CASCADE");
            } else {
                throw new UnsupportedOperationException("Неизвестная БД: ");
            }
        }

    }

}
