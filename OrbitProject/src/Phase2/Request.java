package Phase2;

import java.io.Serializable;
import java.util.HashMap;

/**
 * this class is a serialized class for request and represents a request in this project.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 2724937972241616654L;
    private HashMap<String, String> mp;

    public Request() {
        mp = new HashMap<>();
        mp.put("url", "www.google.com");
        mp.put("method", "GET");
        mp.put("headers", "");
        mp.put("i", "false");
        mp.put("help", "false");
        mp.put("f", "false");
        mp.put("output", "");
        mp.put("save", "false");
        mp.put("data", "");
        mp.put("json", "");
        mp.put("upload", "");
        mp.put("type","encoded");
    }

    public HashMap<String, String> getMp() {
        return mp;
    }
}
