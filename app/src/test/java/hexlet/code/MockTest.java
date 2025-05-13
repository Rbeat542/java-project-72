package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import static org.assertj.core.api.Assertions.assertThat;

public final class MockTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void beforeAll() throws SQLException, TestAbortedException, IOException {
        app = App.getApp();
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("""
        <html>
            <head>
                <title>fake title</title>
                <meta name="description" content="fake description">
            </head>
            <body>
                <h1>fake h1</h1>
            </body>
        </html>
            """)
                .setResponseCode(200)
                .addHeader("Content-Type", "text/html"));
        mockWebServer.start();
    }

    @Test
    public void mockTest() {
        String mockServerUrl = mockWebServer.url("/").toString();
        JavalinTest.test(app, (server, client) -> {
            Url mockUrl = new Url(mockServerUrl);
            mockUrl.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            assertThat(client.post(NamedRoutes.urlsPath(), "url=" + mockServerUrl).code()).isEqualTo(200);

            Long id = UrlRepository.getEntities().getLast().getId();
            client.post(NamedRoutes.urlCheckPath(id));
            Response response = client.get("/urls/1");
            String html = response.body().string();
            assertThat(html).contains("fake title");
            assertThat(html).contains("fake h1");
            assertThat(html).contains("fake description");

            mockWebServer.shutdown();
        });
    }
}
