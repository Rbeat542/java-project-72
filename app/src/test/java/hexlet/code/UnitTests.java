package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public final class UnitTests {
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
    public void beforeEach() throws SQLException {
        UrlRepository.clear();
    }


    @Test
    public void mockTest() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        MockResponse mockResponse = new MockResponse()
                .setBody("fake body")
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        String mockUrlName = mockWebServer.url("/").toString();
        var response = Unirest.get(mockUrlName).asString();
        var body = response.getBody().toString();
        assertThat(body).contains("fake");
        assertThat(body).doesNotContain(Constants.URLNAMECORRECT, Constants.URLNAMEINCORRECT);
        assertThat(response.getStatus()).isEqualTo(200);
        mockWebServer.shutdown();
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
        String now = new Timestamp(System.currentTimeMillis()).toString();

        HttpResponse response = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", now)
                .asString();
        log.info("ATT. Now is " + now + "from test");
        log.info("ATT. Response is " + response.toString());
        var body = response.getBody().toString();
        //log.info("ATT. Body is " + body);
        var url = UrlRepository.getEntities().getFirst();

        assertThat(UrlRepository.getEntities().size()).isEqualTo(1);
        assertThat(url.getName()).isEqualTo(nameExpected);
        assertThat(url.getCreatedAt().toString()).isEqualTo(now.toString());
        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testAddWrongUrl()  {
        String now = new Timestamp(System.currentTimeMillis()).toString();
        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAMEINCORRECT)
                .field("createdAt", now)
                .asString();
        var body = responsePost.getBody().toString();

        assertThat(body).contains("Некорректный URL");
        assertThat(responsePost.getStatus()).isEqualTo(422);
        assertThat(UrlRepository.getEntities()).isEmpty();
    }

    @Test
    public void testShowCorrectIdPage() {
        String now = new Timestamp(System.currentTimeMillis()).toString();

        Unirest.post(baseUrl + "/urls").field("url", Constants.URLNAME).field("createdAt", now).asString();
        var response = Unirest.get(baseUrl + "/urls/1").asString();
        var body = response.getBody().toString();

        assertThat(body).contains(Constants.URLNAMECORRECT);
        assertThat(body).contains(now);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void testUrlCheckToRepository() throws SQLException {
        var nameExpected = Constants.URLNAMECORRECT;
        var now = new Timestamp(System.currentTimeMillis());
        UrlRepository.save(new Url("http://google.com", now));
        Unirest.post(baseUrl + "/urls/1/checks")
                .field("status_code", "200")
                .field("createdAt", now.toString())
                .field("h1", "Url info")
                .field("title", "Url page")
                .field("url_id", "1")
                .field("description", "Description is absent")
                .asString();
        assertThat(UrlCheckRepository.getEntities(1L)).isNotEmpty();
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
    public static void afterAll() throws IOException {
        app.stop();
        //
    }
}
