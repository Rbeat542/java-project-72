package hexlet.code;

import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
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


public class ShowIdPageTestCorrect {
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
    public void showIdPageTestCorrect() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);

        Unirest.post(baseUrl + "/urls").field("url", Constants.URLNAME).field("createdAt", datetime).asString();
        var response = Unirest.get(baseUrl + "/urls/1").asString();
        var body = response.getBody().toString();

        assertThat(body).contains(Constants.URLNAMECORRECT);
        assertThat(body).contains(datetime);
        assertThat(body).doesNotContain("/project/72");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
