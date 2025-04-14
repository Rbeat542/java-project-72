package hexlet.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import hexlet.code.util.NamedRoutes;
import io.javalin.rendering.template.JavalinJte;


public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(7070);
    }

    public static String getDBUrl() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        System.out.println("GET_ENB : " + dbUrl);
        if (null == dbUrl) {
            return "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";
        } else return "jdbc:postgresql://dpg-cvna5949c44c73dl3ik0-a:5432/hexlet_rpoject_db?password=19LkTLBf9Zdc1af6TZUOxHKXmQhY0Jov&user=hexlet_rpoject_db_user";
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        var DB_url = getDBUrl();
        hikariConfig.setJdbcUrl(DB_url);
        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));

        // Получаем соединение, создаем стейтмент и выполняем запрос
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        //BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get(NamedRoutes.root(), ctx -> {
            ctx.render("layout/root.jte");
        });

        return app;
    }
}
