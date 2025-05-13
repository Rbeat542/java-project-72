package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

@Slf4j
public class UrlCheckController {
    public static void check(Context ctx) throws ValidationException, SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        String name = url.getName();
        try {
            var urlCheck = processCheck(name, id);
            UrlCheckRepository.save(urlCheck);
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            log.info("Exception is: " + e);
            Thread.currentThread().interrupt();
        }
    }

    public static UrlCheck processCheck(String name, long urlId) throws IOException, InterruptedException {
        try {
            Connection.Response response = Jsoup.connect(name).execute();
            Document text = response.parse();
            UrlCheck urlCheck = new UrlCheck();
            urlCheck.setTitle(text.title());
            urlCheck.setH1(text.select("h1").text());
            urlCheck.setDescription(text.select("meta[name=description]").attr("content"));
            urlCheck.setUrlId(urlId);
            urlCheck.setStatusCode((long) response.statusCode());
            urlCheck.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
            log.info("Checking URL: " + name);
            log.info("Response status code  = " + response.statusCode());
            log.info("Execution_log: --UrlCheckRepository.save-- Repo size: "
                    + UrlCheckRepository.getEntities(1L).size());
            log.info("Execution_log: --UrlCheckRepository.save-- name: " + urlCheck.getTitle());
            log.info("Execution_log: --UrlCheckRepository.save-- id: " + urlCheck.getId());
            return urlCheck;
        } catch (Exception e) {
            log.info("Exception is: " + e);
            Thread.currentThread().interrupt();
            return new UrlCheck();
        }
    }
}
