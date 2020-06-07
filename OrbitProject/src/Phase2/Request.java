package Phase2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        mp.put("upload", "false");
    }

    public HashMap<String, String> getMp() {
        return mp;
    }
}
