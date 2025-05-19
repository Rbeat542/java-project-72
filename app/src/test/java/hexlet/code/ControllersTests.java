package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

import static hexlet.code.App.getApp;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public final class ControllersTests {
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws SQLException, TestAbortedException {
        app = getApp();
        app.start(7070);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        UrlRepository.clear();
    }

    @Test
    public void testMainEmptyPage() {
        var response = Unirest.get(baseUrl + "/urls").asString();
        String body = response.getBody();
        assertThat(body).contains("No urls added yet!");
        assertThat(body).doesNotContain(Constants.URLCORRECT, Constants.URLINCORRECT);
    }

    @Test
    public void testAddCorrectUrl() {
        var nameExpected = Constants.URLCORRECT;
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .asString();
        log.info("ATT. Response is " + response.toString());
        String body = response.getBody();
        var urls = UrlRepository.getEntities();

        assertThat(urls.size()).isEqualTo(1);
        assertThat(urls.getFirst().getName()).isEqualTo(nameExpected);
        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testAddWrongUrl()  {
        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLINCORRECT)
                .asString();
        var body = responsePost.getBody().toString();

        assertThat(body).contains("Некорректный URL");
        assertThat(responsePost.getStatus()).isEqualTo(422);
        assertThat(UrlRepository.getEntities()).isEmpty();
    }

    @Test
    public void testShowCorrectIdPage() {
        String currentTime = LocalTime.now().toString().substring(0, 4);
        Unirest.post(baseUrl + "/urls").field("url", Constants.URLNAME).asString();
        var response = Unirest.get(baseUrl + "/urls/1").asString();
        String body = response.getBody();

        assertThat(body).contains(Constants.URLCORRECT);
        assertThat(body).contains(currentTime);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testUrlCheckToRepository() throws SQLException {
        var now = new Timestamp(System.currentTimeMillis());
        Url url = new Url(Constants.URLCORRECT);
        UrlRepository.save(url);
        Long id = UrlRepository.getEntities().getLast().getId();
        UrlCheck urlCheck = new UrlCheck(200L, "Url info", "Url h1 section", "No description", id, now.toString());
        UrlCheckRepository.save(urlCheck);
        assertThat(UrlCheckRepository.getEntities(1L)).isNotEmpty();
        assertThat(UrlCheckRepository.getEntities(1L).getLast().getTitle()).isEqualTo("Url info");
    }


    @Test
    public void testCreateCorrectUrl() {
        String now = new Timestamp(System.currentTimeMillis()).toString();
        Unirest.post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", now)
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
