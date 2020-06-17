package Phase4;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    private HashMap<String, List<String>> headers;
    private String body, responseMessage;
    private int responseCode, millis, sz;
    private boolean showHeader;

    public Response(boolean showHeader) {
        this.showHeader = showHeader;
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

    public void setHeaders(HashMap<String, List<String>> headers) {
        this.headers = headers;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
