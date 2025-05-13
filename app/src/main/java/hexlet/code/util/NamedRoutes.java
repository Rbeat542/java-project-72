package hexlet.code.util;

public class NamedRoutes {
    public static String root() {
        return "/";
    }
    public static String build() {
        return "/build";
    }
    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }
    public static String urlPath(Long id) {
        return urlPath(id.toString());
    }

    public static String urlCheckPath(Long id) {
        return urlCheckPath(id.toString());
    }
    public static String urlCheckPath(String id) {
        return  "/urls/" + id + "/checks";
    }
}
