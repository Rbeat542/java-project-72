package hexlet.code.controller;

import hexlet.code.dto.BuildUrlPage;
import hexlet.code.model.Url;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var latestChecks = UrlCheckRepository.getLatestChecks();
        var page = new UrlsPage(urls, latestChecks);
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        page.setFlashText(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", model("page", page));
    }

    public static void mainPage(Context ctx) {
        var page = new BuildUrlPage();
        ctx.render("mainpage.jte", model("page", page));
    }

    public static void create(Context ctx) throws ValidationException, URISyntaxException {
        String nameEntered = ctx.formParam("url");
        String name = "";
        try {
            var urlChecked = new URI(nameEntered).toURL();
            processUrl(ctx, urlChecked);
        } catch (Exception e) {
            var valError = new ValidationError<>(e.toString());
            var list = List.of(valError);
            var errorsMap = new HashMap<String, List<ValidationError<Object>>>();
            errorsMap.put("some", list);
            var page = new BuildUrlPage(nameEntered, errorsMap);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            page.setFlashText(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
            ctx.render("mainpage.jte", model("page", page)).status(422);
        }
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var list = UrlCheckRepository.getEntities(id).reversed();
        var page = new UrlPage(url, list, null);
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        page.setFlashText(ctx.consumeSessionAttribute("flash"));
        ctx.render("show.jte", model("page", page));
    }

    public static void processUrl(Context ctx, URL inputUrl) throws SQLException {
        String name;
        var protocol = inputUrl.getProtocol();
        var host = inputUrl.getHost();
        var port = inputUrl.getPort();
        if (port != -1) {
            name = protocol + "://" + host + ":" + port;
        } else {
            name = protocol + "://" + host;
        }
        var urlFound = UrlRepository.findName(name);
        if (urlFound.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "warning");
        } else {
            var url = new Url(name);
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.status(422);
            log.info("Execution_log: In UrlController.processUrl the known id is: " + url.getId());
        }
        ctx.redirect(NamedRoutes.urlsPath());
    }
}
