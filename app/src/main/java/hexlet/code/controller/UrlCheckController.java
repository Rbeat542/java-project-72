package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.sql.SQLException;


@Slf4j
public class UrlCheckController {
    public static void check(Context ctx) throws ValidationException, SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        try {
            UrlCheck urlCheck = new UrlCheck();
            Connection.Response response = Jsoup.connect(url.getName()).execute();
            Document text = response.parse();
            urlCheck.setTitle(text.title());
            urlCheck.setH1(text.select("h1").text());
            urlCheck.setDescription(text.select("meta[name=description]").attr("content"));
            urlCheck.setUrlId(id);
            urlCheck.setStatusCode((long) response.statusCode());
            log.info("Checking URL: " + url.getName());
            log.info("Response status code  = " + response.statusCode());
            log.info("LOGGING: UrlCheckController.check name: " + urlCheck.getTitle());
            log.info("LOGGING: UrlCheckController.check id: " + urlCheck.getId());
            log.info("LOGGING: UrlCheckController.check title: " + urlCheck.getTitle());
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес: " + e.getMessage());
            ctx.sessionAttribute("flash-type", "danger");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Ошибка : " + e.getMessage());
            ctx.sessionAttribute("flash-type", "danger");
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }


}
