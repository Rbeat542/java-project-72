package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (status_code, title, h1, description,"
                + " url_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        Instant now = Instant.now();
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, Timestamp.from(now));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                urlCheck.setCreatedAt(now);
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getEntities(Long url) {
        var sql = "SELECT * FROM url_checks WHERE url_id=? ORDER BY id ASC";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, url);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            Long rowCount = 0L;
            while (resultSet.next()) {
                rowCount++;
                var id = rowCount;
                var statusCode = resultSet.getLong("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at").toInstant();
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                result.add(urlCheck);
            }
            if (result == null || result.isEmpty()) {
                return new ArrayList<UrlCheck>();
            }
            return result;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public static Map<Long, UrlCheck> getLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        try (var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            Long rowCount = 0L;
            Map<Long, UrlCheck> checks = new HashMap<>();
            while (resultSet.next()) {
                rowCount++;
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getLong("status_code");
                var createdAt = resultSet.getTimestamp("created_at").toInstant();
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var check = new UrlCheck(statusCode, title, h1, description, urlId);
                check.setCreatedAt(createdAt);
                checks.put(urlId, check);
            }
            if (checks == null || checks.isEmpty()) {
                return new HashMap<Long, UrlCheck>();
            }
            return checks;
        }
    }
}
