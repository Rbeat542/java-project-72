package hexlet.code.model;

import hexlet.code.dto.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class UrlCheckPage extends BasePage {
    private List<UrlCheck> urlCheck;
}
