package hexlet.code.dto;

import io.javalin.validation.ValidationError;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@Getter
public final class BuildUrlPage extends BasePage {
    private String name;
    private Timestamp createdAt;
    private Map<String, List<ValidationError<Object>>> errors;

    public BuildUrlPage() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public BuildUrlPage(String name, Map<String, List<ValidationError<Object>>> errors) {
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.errors = errors;
    }
}
