import com.jimplush.goose.*;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.http.Response;
import org.simpleframework.http.Request;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.PrintStream;
import java.util.*;

import net.sf.json.*;

public class URLEndpoint implements Container {
	public static void main(String[] args) throws Exception{
      Container container = new URLEndpoint();
      Connection connection = new SocketConnection(container);
      SocketAddress address = new InetSocketAddress(8890);

      connection.connect(address);
	}

	public void handle(Request request, Response response) {
		try {
	        Configuration config = new Configuration();
	        config.setImagemagickConvertPath("/usr/bin/convert");
	        config.setImagemagickIdentifyPath("/usr/bin/identify");
	        config.setLocalStoragePath("./storage");

	        ContentExtractor contentExtractor = new ContentExtractor(config);
	        String url = "http://techcrunch.com/2010/08/13/gantto-takes-on-microsoft-project-with-web-based-project-management-application/";
	        Article article = contentExtractor.extractContent(url);

	        Map map = new HashMap();
	        map.put("title", article.getTitle());
	        map.put("image", article.getTopImage().getImageSrc());
	        map.put("text", article.getCleanedArticleText());

	        PrintStream body = response.getPrintStream();
	        long time = System.currentTimeMillis();

	        response.set("Content-Type", "application/json");
	        response.setDate("Date", time);
	        response.setDate("Last-Modified", time);

	        body.println(JSONObject.fromObject(map).toString());
	        body.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}