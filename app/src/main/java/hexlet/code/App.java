package hexlet.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import hexlet.code.util.NamedRoutes;
import io.javalin.rendering.template.JavalinJte;

import gg.jte.ContentType;   //stage 4
import gg.jte.TemplateEngine;  //stage 4
import gg.jte.resolve.ResourceCodeResolver;  //stage 4

public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(7070);
    }

    public static String getDbUrl() throws SQLException {
        String dbUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        return  dbUrl;
    }

    private static TemplateEngine createTemplateEngine() {  //stage 4
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        var dbUrl = getDbUrl();
        InputStream url;
        hikariConfig.setJdbcUrl(dbUrl);
        var dataSource = new HikariDataSource(hikariConfig);
        if (dbUrl.contains("jdbc:h2")) {
            url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        } else {
            url = App.class.getClassLoader().getResourceAsStream("schemaPg.sql");
        }
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
        // Получаем соединение, создаем стейтмент и выполняем запрос
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));  //stage 4
        });

        app.get(NamedRoutes.root(), UrlController::index);
        app.get(NamedRoutes.build(), UrlController::build);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);

        return app;
    }
}
