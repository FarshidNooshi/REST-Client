package Phase2;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Run {
    public static void bufferOutFormData(HashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : body.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File(body.get(key))));
                    byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                    tempBufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public static void uploadBinary() {
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(haditabatabaei))) {
            URL url = new URL("http://apapi.haditabatabaei.ir/tests/post/binary");
            File haditabatabaei = new File("haditabatabaei.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            System.out.println(new String(bufferedInputStream.readAllBytes()));
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getHeaderFields());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void formData() {
        HashMap<String, String> fooBody = new HashMap<>();
        fooBody.put("name", "hadi");
        fooBody.put("lastName", "tabatabaei");
        fooBody.put("file", "pic2.png");
        fooBody.put("file2", "result.png");
        try {
            URL url = new URL("http://apapi.haditabatabaei.ir/tests/post/formdata");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String boundary = System.currentTimeMillis() + "";
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            BufferedOutputStream request = new BufferedOutputStream(connection.getOutputStream());
            bufferOutFormData(fooBody, boundary, request);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            System.out.println(new String(bufferedInputStream.readAllBytes()));
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getHeaderFields());
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        uploadBinary();
        formData();
    }
}