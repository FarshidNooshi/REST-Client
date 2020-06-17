package Phase4;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    private HashMap<String, List<String>> headers;
    private String body, responseMessage;
    private int responseCode, millis, sz;

    public Response() {
        headers = new HashMap<>();
        body = "";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = (HashMap<String, List<String>>) headers;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
