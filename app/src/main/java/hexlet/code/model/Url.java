package hexlet.code.model;
import lombok.Setter;

import java.sql.Timestamp;


@Setter
public final class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
