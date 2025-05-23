package hexlet.code.model;
import hexlet.code.dto.BasePage;
import io.javalin.validation.ValidationError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UrlPage extends BasePage {
    private Url url;
    private Map<String, List<ValidationError<Object>>> errors;


    public UrlPage(Url url, Map<String, List<ValidationError<Object>>> errors) {
        this.url = url;
        this.errors = errors;
    }
}
