package hexlet.code;

import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.FormatStyle.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;


public class AddUrlTestCorrect {
    private static Javalin app;
    private static String baseUrl;


    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        app.start(7070);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        UrlRepository.getEntities().clear();
    }

    @Test
    public void addUrlTestCorrect() {
        var nameExpected = Constants.URLNAMECORRECT;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);

        HttpResponse responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", datetime)
                .asString();
        var body = responsePost.getBody().toString();
        var url = UrlRepository.getEntities().get(0);

        assertThat(body).contains(nameExpected);
        assertThat(body).doesNotContain("/project/72");
        assertThat(responsePost.getStatus()).isEqualTo(200);
        assertThat(url.getName()).isEqualTo(nameExpected);
        assertThat(url.getCreatedAt()).isEqualTo(datetime);
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
