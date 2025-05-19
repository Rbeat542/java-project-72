package hexlet.code.dto;

import io.javalin.validation.ValidationError;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public final class BuildCheckUrlPage extends BasePage {
    private String name;
    private Timestamp createdAt;
    private Map<String, List<ValidationError<Object>>> errors;

    public BuildCheckUrlPage() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public BuildCheckUrlPage(String name, Timestamp createdAt, Map<String, List<ValidationError<Object>>> errors) {
        this.name = name;
        this.createdAt = createdAt;
        this.errors = errors;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Map<String, List<ValidationError<Object>>> getErrors() {
        return errors;
    }
}
