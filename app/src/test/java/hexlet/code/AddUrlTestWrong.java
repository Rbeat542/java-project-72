package hexlet.code;

import hexlet.code.repository.UrlRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import io.javalin.Javalin;

import static java.time.format.FormatStyle.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;

public class AddUrlTestWrong {
    private static Javalin app;
    private static String baseUrl;


    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        UrlRepository.getEntities().clear();
    }

    @Test
    public void addUrlTestWrong() throws SQLException, IOException {
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

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
