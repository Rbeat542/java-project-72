package hexlet.code.dto;

import io.javalin.validation.ValidationError;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import static java.time.format.FormatStyle.MEDIUM;

public final class BuildUrlPage extends BasePage {
    private String name;
    private String createdAt;
    private Map<String, List<ValidationError<Object>>> errors;

    public BuildUrlPage() {
        this.createdAt = getCurrentDateTime();
    }

    public String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
        return LocalDateTime.now().format(formatter);
    }

    public BuildUrlPage(String name, String createdAt, Map<String, List<ValidationError<Object>>> errors) {
        this.name = name;
        this.createdAt = createdAt;
        this.errors = errors;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Map<String, List<ValidationError<Object>>> getErrors() {
        return errors;
    }
}
