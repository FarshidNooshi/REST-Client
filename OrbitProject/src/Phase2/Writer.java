package Phase2;

import java.io.*;

public class Writer {
    private ObjectOutputStream out;

    public Writer(String path) throws IOException {
        out = new ObjectOutputStream(new FileOutputStream(new File(path)));
    }

    public void WriteToFile(Object obj) throws IOException {
        out.writeObject(obj);
    }

    public void CloseConnection () throws IOException {
        out.close();
    }
}
