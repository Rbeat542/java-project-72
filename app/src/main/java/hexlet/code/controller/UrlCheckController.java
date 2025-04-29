package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.FormatStyle.MEDIUM;

public class UrlCheckController {

    public static void check(Context ctx) throws ValidationException, URISyntaxException, SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var name = UrlRepository.find(id).get().getName();
        try {
            var urlCheck = processCheck(name, id);
            UrlCheckRepository.save(urlCheck);
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            System.out.println("Something's wrong. Exception is :" + e);
        }
    }

    public static UrlCheck processCheck(String name, long urlId) throws IOException, InterruptedException {
        try {
            Document text = Jsoup.connect(name).get();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(name))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            UrlCheck urlCheck = new UrlCheck();
            urlCheck.setTitle(text.title());
            urlCheck.setH1(text.selectFirst("h1").text());
            urlCheck.setDescription(text.select("meta[name=description]").attr("content"));
            urlCheck.setUrlId(urlId);
            urlCheck.setStatusCode((long) response.statusCode());
            urlCheck.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(MEDIUM)));
            return urlCheck;

        } catch (Exception e) {
            return null;
        }
    }
}
