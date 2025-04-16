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

import gg.jte.ContentType;   //stage 4
import gg.jte.TemplateEngine;  //stage 4
import gg.jte.resolve.ResourceCodeResolver;  //stage 4

public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(7070);
    }


    public static String getDbUrl() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        System.out.println("GET_ENV : " + dbUrl);
        if (null == dbUrl) {
            return "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";
        } else {
            return "jdbc:postgresql://dpg-cvna5949c44c73dl3ik0-a:5432/hexlet_rpoject_db?password"
                + "=19LkTLBf9Zdc1af6TZUOxHKXmQhY0Jov&user=hexlet_rpoject_db_user";
        }
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
        hikariConfig.setJdbcUrl(dbUrl);
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


        /*
        public static void main(String[] args) {
            var app = Javalin.create(*//*config*//*)
                    .get("/", ctx -> ctx.result("Hello World"))
                    .start(7070);
        }
    }
    */


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));  //stage 4
        });

        app.get(NamedRoutes.root(), ctx -> {
            //ctx.render("root.jte");
            ctx.render("root.jte");
        });

        return app;
    }
}
