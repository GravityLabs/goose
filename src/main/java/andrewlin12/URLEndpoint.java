import com.jimplush.goose.*;
import com.jimplush.goose.images.Image;
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

	public static String encodeHTML(String s)
	{
		if (s == null) {
			return "";
		}

	    StringBuffer out = new StringBuffer();
	    for(int i=0; i<s.length(); i++)
	    {
	        char c = s.charAt(i);
	        if(c > 127 || c=='"' || c=='<' || c=='>')
	        {
	           out.append("&#"+(int)c+";");
	        }
	        else
	        {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}


	public void handle(Request request, Response response) {
		try {
	        PrintStream body = response.getPrintStream();
	        Map map = new HashMap();

			String url = request.getQuery().get("url");
			if (url == null || url.length() == 0) {
				map.put("error", true);
				map.put("message", "No URL specified");
			}
			else {
		        Configuration config = new Configuration();
		        config.setImagemagickConvertPath("/usr/bin/convert");
		        config.setImagemagickIdentifyPath("/usr/bin/identify");
		        config.setLocalStoragePath("./storage");

		        ContentExtractor contentExtractor = new ContentExtractor(config);
		        Article article = contentExtractor.extractContent(url);

		        map.put("success", true);
		        map.put("title", encodeHTML(article.getTitle()));
		        Image image = article.getTopImage();
		        if (image != null) {
			        map.put("image", article.getTopImage().getImageSrc());
		        }
		        map.put("images", article.getImageCandidates());
		        map.put("link", article.getCanonicalLink());
		        map.put("text", encodeHTML(article.getCleanedArticleText()));
			}

	        body.println(JSONObject.fromObject(map).toString());

	        response.set("Content-Type", "application/json");

	        long time = System.currentTimeMillis();
	        response.setDate("Date", time);
	        response.setDate("Last-Modified", time);
	        body.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}