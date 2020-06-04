package Phase2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPService {
    private URL url;
    private HttpURLConnection connection;
    private int statusCode;

    public HTTPService(String url) {
        try {
            this.url = new URL(url);
            connection = (HttpURLConnection) this.url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runService() {
        try {
            statusCode = connection.getResponseCode();
            System.out.println(statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
