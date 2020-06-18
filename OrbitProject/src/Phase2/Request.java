package Phase2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

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
        mp.put("type", "");
        mp.put("uploadBinary", "");
        mp.put("proxy", "false");
        mp.put("ip", "127.0.0.1");
        mp.put("port", "1726");
    }

    /**
     * this method will save the request to the path that user gave.
     *
     * @throws IOException if the exception occurred
     */
    public void SaveRequest() throws IOException {
        String path = new File(mp.get("save")).getAbsolutePath();
        String name = (Objects.requireNonNull(new File(path).list()).length + 1) + ".AUT";
        Writer writer = new Writer(path + File.separator + name);
        writer.WriteToFile(this);
    }

    public HashMap<String, String> getMp() {
        return mp;
    }
}
