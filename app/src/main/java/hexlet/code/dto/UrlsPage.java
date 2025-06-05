package hexlet.code.dto;
import hexlet.code.model.LastCheck;
import hexlet.code.model.Url;
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
    private Map<Long, LastCheck> lastStatuses;
}
