package Phase2;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * this class represents a service for the user http requests and stuff.
 */
public class HTTPService {
    private HttpURLConnection connection;
    private int statusCode;
    private Request request;

    /**
     * is the constructor for our service
     * @param req is the request that we wanna execute
     */
    public HTTPService(Request req) {
        request = req;
    }

    /**
     * runs the servcie for the client, e.x. executes the request
     * @throws MalformedURLException for the url connection
     */
    public void runService() throws MalformedURLException {
        URL url = new URL(request.getMp().get("url"));
        try {
            if ("http".equals(url.getProtocol())) {
                connection = (HttpURLConnection) url.openConnection();
            } else if ("https".equals(url.getProtocol())) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                System.err.println("UNSUPPORTED PROTOCOL!");
                return;
            }

            initHeaders(connection);
            connection.setRequestMethod(request.getMp().get("method"));
            connection.connect();

            // Make sure response code is in the 200 range.
            if ((statusCode = connection.getResponseCode()) / 100 != 2)
                throw new IOException(statusCode + connection.getResponseMessage());
        } catch (IOException ex) {
            System.err.println("FAILED TO OPEN CONNECTION!" + ex);
            return;
        }


    }

    private void initHeaders(HttpURLConnection connection) {
        String input = request.getMp().get("headers") + ";";
        int size = input.length();
        for (int i = 0; i < size; i++) {
            String key = null, value = null;
            int j = i;
            while (j < size && input.charAt(j) != ':')
                j++;
            if (j < size)
                key = input.substring(i, j);
            i = ++j;
            while (j < size && input.charAt(j) != ';')
                j++;
            if (i < size)
                value = input.substring(i, j);
            if (value != null && key != null)
                connection.addRequestProperty(key, value);
            i = j;
        }
    }
}
