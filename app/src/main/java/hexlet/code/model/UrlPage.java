package hexlet.code.model;
import hexlet.code.dto.BasePage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UrlPage extends BasePage {
    private Url url;

    public UrlPage(Url url) {
        this.url = url;
    }
}