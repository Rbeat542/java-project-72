package hexlet.code.controller;

import hexlet.code.dto.BuildUrlPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlPage;
import hexlet.code.model.UrlsPage;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {

    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", model("page", page));
    }

    public static void build(Context ctx) {
        var page = new BuildUrlPage();
        ctx.render("build.jte", model("page",page));
    }

    public static void create(Context ctx) throws ValidationException, URISyntaxException {
        var nameEntered = ctx.formParam("url");
        var createdAt = ctx.formParam("createdAt");
        var name ="";
        try {
            var urlChecked = new URI(nameEntered).toURL();
            var protocol = urlChecked.getProtocol();
            var host = urlChecked.getHost();
            var port = urlChecked.getPort();
            if (port != -1) {
                name =  protocol + "://" + host + ":" + port;
            } else {
                name =  protocol + "://" + host;
            }
            processUrl(ctx, name, createdAt);
        } catch (Exception e) {
            var valError = new ValidationError<>(e.toString());
            var list = List.of(valError);
            var errorsMap = new HashMap<String, List<ValidationError<Object>>>();
            errorsMap.put("some", list);
            var page = new BuildUrlPage(nameEntered, createdAt, errorsMap);
            ctx.sessionAttribute("flash", "Некорректный URL");
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("build.jte", model("page", page));
        }

    }

    public static void show(Context ctx) throws SQLException{
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new UrlPage(url);
        ctx.render("show.jte", model("page",page));
    }

    /*public static void processUrl(Context ctx, String name, String createdAt ) throws SQLException {
        if (UrlRepository.getEntities().isEmpty()) {
            var url = new Url(name, createdAt);
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.redirect(NamedRoutes.root());
        } else {
            for (var urlFromRepository : UrlRepository.getEntities()) {
                if (urlFromRepository.getName().equals(name)) {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.redirect(NamedRoutes.root());
                } else {
                    var url = new Url(name, createdAt);
                    UrlRepository.save(url);
                    ctx.sessionAttribute("flash", "Страница успешно добавлена");
                    ctx.redirect(NamedRoutes.root());
                }
            }
        }
    }*/

    public static void processUrl(Context ctx, String name, String createdAt ) throws SQLException {
        var urls = UrlRepository.getEntities();
        var isAlreadyExists = urls.stream()
                .anyMatch(url -> url.getName().equals(name));
        if (isAlreadyExists) {
            ctx.sessionAttribute("flash", "Страница уже существует");
        } else {
            var url = new Url(name, createdAt);
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
        }
        ctx.redirect(NamedRoutes.root());
    }
}
