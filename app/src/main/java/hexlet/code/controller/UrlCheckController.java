package hexlet.code.controller;

import hexlet.code.ResponseHandler;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.FormatStyle.MEDIUM;

public class UrlCheckController {

    public static void check(Context ctx) throws ValidationException, URISyntaxException, SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var name = UrlRepository.find(id).get().getName();
        try {
            var urlCheck = new UrlCheck();
            ResponseHandler.init(name);
            urlCheck.setUrlId(id);
            urlCheck.setStatusCode((long) ResponseHandler.getStatus());
            urlCheck.setH1(ResponseHandler.getH1());
            urlCheck.setTitle(ResponseHandler.getTitle());
            urlCheck.setDescription(ResponseHandler.getDescription());
            urlCheck.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(MEDIUM)));
            UrlCheckRepository.save(urlCheck);
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            System.out.println("Something's wrong. Exception : " + e);
        }

    }
}
