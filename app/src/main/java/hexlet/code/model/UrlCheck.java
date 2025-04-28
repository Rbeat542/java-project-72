package hexlet.code.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public final class UrlCheck {
    private Long id;
    private Long statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getDescription() {
        return description;
    }

    public Long getUrlId() {
        return urlId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UrlCheck(Long statusCode, String title, String h1, String description, Long urlId, String createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
}
