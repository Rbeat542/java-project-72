package hexlet.code.util;

import hexlet.code.model.Url;
import hexlet.code.dto.BuildUrlPage;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import java.sql.SQLException;
import static io.javalin.rendering.template.TemplateUtil.model;

public class NamedRoutes {
    public static String root() {
        return "/";
    }

    public static String build() {
        return "/build";
    }

    public static String urlsPath() {return "/urls"; }

    public static String urlPath(String id) {return "/urls/" + id; }
    public static String urlPath(Long id) {return urlPath(id.toString()); }

}
