package hexlet.code;

import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;


public class BlankMainPageTest {
    private static Javalin app;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void blankMainPageTest() throws SQLException, IOException {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/").code()).isEqualTo(200);
            assertThat(client.get("/").body().string()).contains("No urls added yet!");
            assertThat(UrlRepository.getEntities()).isEmpty();
        });
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
