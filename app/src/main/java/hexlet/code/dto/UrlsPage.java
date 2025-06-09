package hexlet.code.dto;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class UrlsPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> latestChecks;
}
