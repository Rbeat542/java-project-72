package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.FormatStyle.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;

public class UnitTests {
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        app = App.getApp();
        app.start(7070);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @BeforeEach
    public final void beforeEach() throws SQLException, IOException {
        UrlRepository.clear();
    }

    @Test
    public void testMainEmptyPage() {
        var response = Unirest.get(baseUrl + "/").asString();
        var body = response.getBody().toString();
        assertThat(body).contains("No urls added yet!");
        assertThat(body).doesNotContain(Constants.URLNAMECORRECT, Constants.URLNAMEINCORRECT);
    }

    @Test
    public void testAddCorrectUrl() {
        var nameExpected = Constants.URLNAMECORRECT;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        final var datetime = LocalDateTime.now().format(formatter);

        HttpResponse response = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", datetime)
                .asString();
        var body = response.getBody().toString();
        var url = UrlRepository.getEntities().getFirst();

        assertThat(UrlRepository.getEntities().size()).isEqualTo(1);
        assertThat(url.getName()).isEqualTo(nameExpected);
        assertThat(url.getCreatedAt()).isEqualTo(datetime);
        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testAddWrongUrl() throws SQLException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);

        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAMEINCORRECT)
                .field("createdAt", datetime)
                .asString();
        var body = responsePost.getBody().toString();

        assertThat(body).contains("Некорректный URL");
        assertThat(responsePost.getStatus()).isEqualTo(422);
        assertThat(UrlRepository.getEntities()).isEmpty();
    }

    @Test
    public void testShowCorrectIdPage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);

        Unirest.post(baseUrl + "/urls").field("url", Constants.URLNAME).field("createdAt", datetime).asString();
        var response = Unirest.get(baseUrl + "/urls/1").asString();
        var body = response.getBody().toString();

        assertThat(body).contains(Constants.URLNAMECORRECT);
        assertThat(body).contains(datetime);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testAddCorrectUrlToRepository() {
        var nameExpected = Constants.URLNAMECORRECT;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        final var datetime = LocalDateTime.now().format(formatter);

        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", datetime)
                .asString();
        var body = responsePost.getBody().toString();
        var url = UrlRepository.getEntities().get(0);

        assertThat(url.getName()).isEqualTo(nameExpected);
        assertThat(url.getCreatedAt()).isEqualTo(datetime);
        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(responsePost.getStatus()).isEqualTo(200);
        assertThat(UrlCheckRepository.getEntities(0L)).isEmpty();
    }

    @Test
    public void testUrlCheckToRepository() throws SQLException {
        var nameExpected = Constants.URLNAMECORRECT;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        final var datetime = LocalDateTime.now().format(formatter);
        UrlRepository.save(new Url("http://google.com", datetime));
        Unirest.post(baseUrl + "/urls/1/checks")
                .field("status_code", "200")
                .field("createdAt", datetime)
                .field("h1", "Url info")
                .field("title", "Url page")
                .field("url_id", "1")
                .field("description", "Description is absent")
                .asString();
        assertThat(UrlCheckRepository.getEntities(1L)).isNotEmpty();
    }


    @Test
    public void testA00ddWrongUrl() throws SQLException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);
        UrlRepository.clear();
        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAMEINCORRECT)
                .field("createdAt", datetime)
                .asString();
        var body = responsePost.getBody().toString();

        assertThat(body).contains("Некорректный URL");
        assertThat(responsePost.getStatus()).isEqualTo(422);
        assertThat(UrlRepository.getEntities()).isEmpty();
    }

    @Test
    public void testCreateCorrectUrl() {
        var datetime = LocalDateTime.now().toString();
        Unirest.post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", datetime)
                .asString();
        HttpResponse response = Unirest
                .get(baseUrl + "/urls/2")
                .asString();
        var list = UrlRepository.getEntities();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(list.size()).isEqualTo(1);
    }



    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
