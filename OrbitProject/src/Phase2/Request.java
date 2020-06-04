package Phase2;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Request {
    HashMap<String, String> mp;

    public Request() {
        mp = new HashMap<>();
        mp.put("URL", "https://www.google.com");
        mp.put("method", "GET");
        mp.put("headers", "");
        mp.put("i", "false");
        mp.put("help", "false");
        mp.put("f", "false");
        mp.put("output", "");
        mp.put("save", "false");
        mp.put("data", "");
        mp.put("json", "");
    }

    public HashMap<String, String> getMp() {
        return mp;
    }
}
