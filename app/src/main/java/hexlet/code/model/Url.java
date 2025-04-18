package hexlet.code.model;
import lombok.Setter;


@Setter
public class Url {
    private Long id;
    private String name;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Url(String name, String createdAt) {
        this.name = name;
        this.createdAt = createdAt;


    }
}
