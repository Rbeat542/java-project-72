package hexlet.code;

import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public final class MockTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void beforeAll() throws SQLException, TestAbortedException, IOException {
        Path path = Paths.get("src", "test", "resources", "fixtures", "TestHtmlPage")
                .toAbsolutePath().normalize();
        String fileContent = Files.readString(path);

        app = App.getApp();
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fileContent)
                .addHeader("Content-Type", "text/html"));
        mockWebServer.start();
    }

    @Test
    public void mockTest() {
        String mockServerUrl = mockWebServer.url("/").toString();
        JavalinTest.test(app, (server, client) -> {
            String url = "url=" + mockServerUrl;
            assertThat(client.post(NamedRoutes.urlsPath(), url).code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities().getLast().getName() + "/").isEqualTo(mockServerUrl);

            Long id = UrlRepository.getEntities().getLast().getId();
            Response checkResponse = client.post(NamedRoutes.urlCheckPath(id), "");
            String htm = checkResponse.body().string();
            assertThat(checkResponse.code()).isEqualTo(200);

            Response response = client.get("/urls/" + id);
            String html = response.body().string();
            assertThat(html).contains("Fake title");
            assertThat(html).contains("Here is the H1 size header");
            assertThat(html).contains("Some description");
            assertThat(UrlCheckRepository.getEntities(id).getLast().getTitle()).isEqualTo("Fake title");
        });
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }
}
