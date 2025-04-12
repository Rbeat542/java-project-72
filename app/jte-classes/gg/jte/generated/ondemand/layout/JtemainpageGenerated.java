package gg.jte.generated.ondemand.layout;
import hexlet.code.util.NamedRoutes;
import gg.jte.Content;
public final class JtemainpageGenerated {
	public static final String JTE_NAME = "layout/mainpage.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,15,15,15,15,17,17,17,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content) {
		jteOutput.writeContent("\n<!doctype html>\n<html lang=\"en\">\n    <head>\n        <meta charset=\"utf-8\" />\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n        <title>Root folder</title>\n    </head>\n    <body>\n        <p>\n\n        </p>\n        ");
		jteOutput.setContext("body", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\n    </body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, content);
	}
}
