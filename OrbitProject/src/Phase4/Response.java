package Phase4;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    private Map<String, List<String>> headers;
    private String body, responseMessage;
    private int responseCode, millis, sz;
    private boolean showHeader;

    public Response(boolean showHeader) {
        this.showHeader = showHeader;
        headers = new HashMap<>();
        body = "";
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public int getMillis() {
        return millis;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getSz() {
        return sz;
    }

    public void setSz(int sz) {
        this.sz = sz;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(responseCode).append("\n").append(responseMessage).append('\n').append(millis).append('\n');
        builder.append("--body\n");
        builder.append(body).append('\n');
        if (showHeader) {
            builder.append("--headers\n");
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                builder.append("Key: ").append(entry.getKey()).append(" ,Value: ").append(entry.getValue()).append("\n");
            }
        }
        return builder.toString();
    }
}
