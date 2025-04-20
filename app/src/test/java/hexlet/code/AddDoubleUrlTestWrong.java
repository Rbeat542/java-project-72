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

public class AddDoubleUrlTestWrong {
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
    public void addDoubleUrlTestWron() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        var datetime = LocalDateTime.now().format(formatter);

        Unirest.post(baseUrl + "/urls")
                .field("url", Constants.URLNAME)
                .field("createdAt", datetime)
                .asString();
        HttpResponse responseDoubled = Unirest
                .post(baseUrl + "/urls")
                .field("url", (Constants.URLNAME + "/some/directory"))
                .field("createdAt", datetime)
                .asString();
        var body = responseDoubled.getBody().toString();

        assertThat(body).contains("Страница уже существует");
        assertThat(responseDoubled.getStatus()).isEqualTo(200);
        assertThat(UrlRepository.getEntities().size()).isEqualTo(1);
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
}
