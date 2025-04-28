package hexlet.code;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseHandler {
    private static String body;
    private static String name;
    private static HttpResponse<String> response;

    public static void init(String nameIn) throws Exception {
        name = nameIn;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(nameIn))
                    .build();
            var temp = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, temp);
            body = response.body();
        } catch (Exception e) {
            body = "";
            System.out.println("Exception : " + e.toString());
        }
    }


    public static int getStatus() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(name))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getH1() {
        var list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?<=>)[^<]+?(?=<\\/span>\\"
                 + "s*<\\/h1>)|(?<=>)[^<]+?(?=<\\/h1>)", Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = pattern.matcher(body);
            while (matcher.find()) {
                list.add(body.substring(matcher.start(), matcher.end()));
            }
            return list.getFirst().toString();
        } catch (Exception e) {
            return ("No <h1> tag found");
        }
    }


    public static String getTitle() {
        var list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?<=\\<title\\>).+?(?=\\<\\/title\\>)", Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = pattern.matcher(body);
            while (matcher.find()) {
                list.add(body.substring(matcher.start(), matcher.end()));
            }
            return list.getFirst().toString();
        } catch (Exception e) {
            return ("No <title> tag found");
        }
    }

    public static String getDescription() {
        var list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?<=\"description\"\\scontent=\").+?(?=\")",
                Pattern.CASE_INSENSITIVE);
        //Pattern pattern = Pattern.compile("(?<=\\\"description\\\"\\scontent\\=\\\").+(?=\\\"\\>)",
        // Pattern.CASE_INSENSITIVE); // no works
        //Pattern pattern = Pattern.compile("(?<=\"description\"\\scontent=\").+?(?=\")",
        // Pattern.CASE_INSENSITIVE); // no works
        try {
            Matcher matcher = pattern.matcher(body);
            while (matcher.find()) {
                list.add(body.substring(matcher.start(), matcher.end()));
            }
            return list.getFirst().toString();
        } catch (Exception e) {
            return ("No <description> tag found");
        }
    }
}
