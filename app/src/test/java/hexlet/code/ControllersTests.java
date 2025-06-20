package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import java.sql.SQLException;
import static hexlet.code.App.getApp;
import static org.assertj.core.api.Assertions.assertThat;

public final class ControllersTests {
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws SQLException, TestAbortedException {
        app = getApp();
        app.start(App.getPort());
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        UrlRepository.clear();
    }

    @Test
    public void testPort() {
        var port = System.getenv().get("PORT");
        assertThat(null == port || 7070 == Integer.parseInt(port));

    }

    @Test
    public void testMainPage() {
        var response = Unirest.get(baseUrl + "/urls").asString();
        String body = response.getBody();
        assertThat(body).contains("No urls added yet!");
        assertThat(body).doesNotContain(Constants.URLCORRECT, Constants.URLINCORRECT);

        response = Unirest.get(baseUrl).asString();
        body = response.getBody();
        var urls = UrlRepository.getEntities();
        assertThat(urls.isEmpty());
        assertThat(body).contains("Url analyzer. Main page");
    }

    @Test
    public void testAddCorrectUrl() {
        var nameExpected = Constants.URLCORRECT;
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .asString();
        String body = response.getBody();
        var urls = UrlRepository.getEntities();

        assertThat(urls.size()).isEqualTo(1);
        assertThat(urls.getFirst().getName()).isEqualTo(nameExpected);
        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);

        var repeatedResponse = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .asString();
        body = repeatedResponse.getBody();
        assertThat(body).contains("Страница уже существует");
        assertThat(UrlRepository.getEntities().size()).isEqualTo(1);
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
    public void testShowCorrectPage() {
        var name = Constants.URLCORRECT;
        Unirest.post(baseUrl + "/urls").field("url", name).asString();
        var id = UrlRepository.findName(name).get().getId();
        var response = Unirest.get(baseUrl + "/urls/" + id).asString();
        String body = response.getBody();

        assertThat(body).contains(Constants.URLCORRECT);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testChecksRepository() throws SQLException {
        Url url = new Url(Constants.URLCORRECT);
        UrlRepository.save(url);
        Long id = UrlRepository.getEntities().getLast().getId();
        UrlCheck urlCheck = new UrlCheck(200L, "Url info", "Url h1 section", "No description", id);
        UrlCheckRepository.save(urlCheck);
        assertThat(UrlCheckRepository.getEntities(1L)).isNotEmpty();
        assertThat(UrlCheckRepository.getEntities(1L).getLast().getTitle()).isEqualTo("Url info");
    }

    @Test
    public void testRepositorySize() throws SQLException {
        Unirest.post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .asString();
        HttpResponse response = Unirest
                .get(baseUrl + "/urls/2")
                .asString();
        var list = UrlRepository.getEntities();
        var checks = UrlCheckRepository.getLatestChecks();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(checks).isEmpty();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void testLatestChecks() throws SQLException {
        Url url = new Url(baseUrl);
        UrlRepository.save(url);
        var id = url.getId();
        Unirest.post(baseUrl + "/urls/" + id + "/checks").asString();

        var checks = UrlCheckRepository.getLatestChecks();

        assertThat(checks.size()).isEqualTo(1);
        assertThat(checks.get(1L).getStatusCode()).isEqualTo(200);
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
