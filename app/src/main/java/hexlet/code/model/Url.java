package hexlet.code.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Url {
    private Integer id;
    private String name;
    private String createdAt;

}
