package hexlet.code;

import java.io.IOException;
import java.sql.SQLException;
import com.sun.tools.javac.Main;
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

    public static Javalin getApp() throws IOException, SQLException {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get(NamedRoutes.root(), ctx -> {
            ctx.render("root.jte");
        });

        return app;
    }
}
