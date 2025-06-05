package hexlet.code.dto;

import io.javalin.validation.ValidationError;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.time.Instant;

@Getter
public final class BuildUrlPage extends BasePage {
    private String name;
    private Instant createdAt;
    private Map<String, List<ValidationError<Object>>> errors;

    public BuildUrlPage() {
        this.createdAt = Instant.now();
    }

    public BuildUrlPage(String name, Map<String, List<ValidationError<Object>>> errors) {
        this.name = name;
        this.createdAt = Instant.now();
        this.errors = errors;
    }
}
