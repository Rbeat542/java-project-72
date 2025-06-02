package hexlet.code.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public final class UrlCheck {
    private Long id;
    private Long statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private String createdAt;

    public UrlCheck(Long statusCode, String title, String h1, String description, Long urlId, String createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
}
