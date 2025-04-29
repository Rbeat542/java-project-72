package hexlet.code;

import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.core.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class MockTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        app.start(7070);

        mockWebServer = new MockWebServer();
        mockWebServer.start();
        MockResponse mockResponse = new MockResponse()
                .setBody("fake body")
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void beforeEach() throws SQLException {
        UrlRepository.clear();
    }

    @Test
    public void mainTest() {
        String mockUrlName = mockWebServer.url("/").toString();
        var response = Unirest.get(mockUrlName).asString();
        var body = response.getBody().toString();
        assertThat(body).contains("fake");
        assertThat(body).doesNotContain(Constants.URLNAMECORRECT, Constants.URLNAMEINCORRECT);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        mockWebServer.close();
    }
}
