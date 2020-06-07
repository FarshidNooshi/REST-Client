package Phase2;

import java.io.*;

public class Reader {
    private ObjectInputStream in;

    public Reader(String path) throws IOException {
        in = new ObjectInputStream(new FileInputStream(new File(path)));
    }

    public Object ReadFromFile() throws IOException {
        return in.read();
    }

    public void CloseConnection () throws IOException {
        in.close();
    }
}
