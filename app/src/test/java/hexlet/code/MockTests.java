package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.NamedRoutes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

class MockTests {
    private static Javalin app;
    private static MockWebServer mockWebServer;
    public static String htmlText;

    @BeforeAll
    static void initMock() throws Exception {
        Path path = Paths.get("src", "test", "resources", "fixtures", "TestHtmlFile");
        htmlText = Files.readString(path);
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        MockResponse mockResponse = new MockResponse()
                .setBody(htmlText)
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void beforeEach() throws IOException, SQLException {
        app = App.getApp();

    }

    @Test
    public void testWithMock() {
        JavalinTest.test(app, (server, client) -> {
            try {
                String mockUrlName = mockWebServer.url("/").toString();
                Url mockUrl = new Url(mockUrlName, LocalDateTime.now().toString());
                UrlRepository.save(mockUrl);
                var path = NamedRoutes.urlCheckPath(mockUrl.getId());
                Map<String, String> formParams = new HashMap<>();
                formParams.put("name", mockUrlName);
                var response = client.post(path, formParams);
                assertThat(response.code()).isEqualTo(200);
                UrlCheck checkedUrl = UrlCheckRepository.getEntities(mockUrl.getId()).get(0);
                assertThat(checkedUrl.getStatusCode()).isEqualTo(200);
                assertThat(checkedUrl.getTitle()).isEqualTo("Web pages analyzer");
                assertThat(checkedUrl.getH1()).isEqualTo("Here is some header with h1 size");
                assertThat(checkedUrl.getDescription()).isEqualTo("Some description");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterAll
    static void afterAll() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }
}
