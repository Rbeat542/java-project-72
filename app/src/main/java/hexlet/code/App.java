package hexlet.code;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlController;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import hexlet.code.util.NamedRoutes;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

@Slf4j
public class App {
    public static void main(String[] args) throws SQLException {
        var app = getApp();
        app.start(getPort());
    }

    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static String getDbUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() throws SQLException {
        var hikariConfig = new HikariConfig();
        var dbUrl = getDbUrl();
        log.info("Execution_log: JDBC_DATABASE_URL = " + dbUrl);
        InputStream url;
        hikariConfig.setJdbcUrl(dbUrl);
        var dataSource = new HikariDataSource(hikariConfig);
        if (dbUrl.contains("jdbc:h2")) {
            url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        } else if (dbUrl.contains("postgres")) {
            url = App.class.getClassLoader().getResourceAsStream("schemaPg.sql");
        } else {
            throw new UnsupportedOperationException(
            "Ошибка. Поддерживаются только базы данных H2 и PostgreSQL");
        }
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        app.get(NamedRoutes.root(), UrlController::mainPage);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlCheckController::check);
        return app;
    }

}
