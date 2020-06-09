package Phase2;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class represents a service for the user http requests and stuff.
 */
public class HTTpService {
    private HttpURLConnection connection;
    private String boundary;
    private Request request;

    /**
     * is the constructor for our service
     *
     * @param req is the request that we wanna execute
     */
    public HTTpService(Request req) {
        request = req;
        boundary =  "" + System.currentTimeMillis();
    }

    /**
     * this method is a copy of the same method of the run class from the authors of the project
     * @param body is a hashMap for representing the body of the request.
     * @param boundary is a boundary for using in the request.
     * @param bufferedOutputStream is the outputStream of the request.
     * @throws IOException if the IO exception occurs.
     */
    private static void bufferOutFormData(HashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
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

    /**
     * runs the service for the client, e.x. executes the request
     *
     * @throws MalformedURLException for the url connection
     */
    public void runService() throws Exception {
        if (!request.getMp().get("data").equals("") && !request.getMp().get("json").equals("")) {
            System.err.println("Program terminated.");
            throw new Exception("can't have both json & form data as message body!");
        }
        if (request.getMp().get("help").equals("true"))
            PrintHelp();
        URL url = new URL(request.getMp().get("url"));
        try {
            if ("http".equals(url.getProtocol())) {
                connection = (HttpURLConnection) url.openConnection();
            } else if ("https".equals(url.getProtocol())) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                System.err.println("UNSUPPORTED PROTOCOL!");
                return;
            }
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            initHeaders(connection);
            connection.setRequestMethod(request.getMp().get("method"));
            initBody(connection);
            while (connection.getResponseCode() / 100 == 3 && request.getMp().get("f").equals("true")) {
                String location = connection.getHeaderField("Location");
                System.out.println("redirected to " + location);
                connection = (HttpURLConnection) (new URL(location)).openConnection();
            }
            String ret = getResponse(connection);
            System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());
            System.out.println(ret);
            if (!request.getMp().get("output").equals("")) {
                File file = new File(new File(System.getProperty("user.dir")).getParent() + File.separator + "OutputFolder");
                file.mkdir();
                String name = request.getMp().get("output");
                File actualFile = new File(file, name);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(actualFile));
                bufferedWriter.write(ret);
                bufferedWriter.flush();
            }
            if (request.getMp().get("i").equals("true")) {
                for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet())
                    System.out.println("Key: " + entry.getKey() + " ,Value: " + entry.getValue() + "\n");
            }
        } catch (IOException ex) {
            System.err.println("FAILED TO OPEN CONNECTION!" + ex);
        }
    }

    /**
     * doing a get request for the connection given to it.
     *
     * @param connection is a connection that user wants to send a get request with desired params.
     * @return a String containing the response body.
     */
    private String Get(HttpURLConnection connection) {
        StringBuilder builder = new StringBuilder();
        String line;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            while ((line = in.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return builder.toString();
    }

    /**
     * this method adds the headers of the user input to our connection
     *
     * @param connection is the http connection to the server
     */
    private void initHeaders(HttpURLConnection connection) {
        if (request.getMp().get("type").equals("json")) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
        } else if (request.getMp().get("type").equals("formData"))
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        else if (request.getMp().get("type").equals("encoded"))
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        else if (request.getMp().get("type").equals("binary"))
            connection.setRequestProperty("Content-Type", "application/octet-stream");
        String input = request.getMp().get("headers") + ";";
        int size = input.length();
        for (int i = 0; i < size; i++) {
            String key = null, value = null;
            int j = i;
            while (j < size && input.charAt(j) != ':')
                j++;
            if (j < size)
                key = input.substring(i, j);
            i = ++j;
            while (j < size && input.charAt(j) != ';')
                j++;
            if (i < size)
                value = input.substring(i, j);
            if (value != null && key != null)
                connection.addRequestProperty(key, value);
            i = j;
        }
    }

    /**
     * initializes the request body for the program.
     *
     * @param connection is the connection to use.
     */
    private void initBody(HttpURLConnection connection) {
        String data;
        if (request.getMp().get("type").equalsIgnoreCase("json")) {
            data = JsonBodyBuilder();
            SendData(connection, data);
        } else if (request.getMp().get("type").equalsIgnoreCase("formData"))
            FormDataBuilder();
        else if (request.getMp().get("type").equalsIgnoreCase("encoded")) {
            data = request.getMp().get("data");
            SendData(connection, data);
        } else if (request.getMp().get("type").equalsIgnoreCase("binary"))
            UploadBinary(request.getMp().get("uploadBinary"));
    }

    /**
     * this method uploads a binary file from the system for the connection.
     * @param uri is the address of the file to upload.
     */
    private void UploadBinary(String uri) {
        File file = new File(uri);
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream())) {

            bufferedOutputStream.write(fileInputStream.readAllBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will write the data to the connection.
     * @param connection is the URL connection to use for writing.
     * @param data is the preferred data for writing.
     */
    private void SendData(HttpURLConnection connection, String data) {
        try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
            out.write(data.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * this method will extract the key&values from the user input to a hashMap and send them for the authors method as they ordered.
     */
    private void FormDataBuilder() {
        String str = request.getMp().get("data");
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            int j = i;
            while (str.charAt(j) != '=')
                j++;
            String key = str.substring(i, j);
            i = j + 1;
            while (j < str.length() && str.charAt(j) != '&')
                j++;
            String value = str.substring(i, j);
            hashMap.put(key, value);
            i = j;
        }
        try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
            bufferOutFormData(hashMap, boundary, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this is a simple json body maker is that builds the json body from the user input.
     * @return a string containing the json body to send for the connection.
     */
    private String JsonBodyBuilder() {
        String data;
        StringBuilder builder = new StringBuilder("{");
        String input = request.getMp().get("json");
        int size = input.length();
        for (int i = 1; i + 1 < size; i++) {
            String key = null, value = null;
            int j = i;
            while (j < size && input.charAt(j) != ':')
                j++;
            if (j < size)
                key = input.substring(i, j);
            i = ++j;
            while (j + 1 < size && input.charAt(j) != ',')
                j++;
            if (i + 1 < size)
                value = input.substring(i, j);
            builder.append("\"").append(key).append("\"").append(":\"").append(value).append("\"");
            i = j;
            if (i + 1 < size)
                builder.append(",");
        }
        builder.append("}");
        data = builder.toString();
        return data;
    }

    /**
     * returns a string showing the status of the failed request or returning the response body.
     *
     * @param connection is the connection to use.
     * @return a string showing the status of the failed request or returning the response body.
     * @throws IOException if the IO exception occurs.
     */
    private String getResponse(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() / 100 == 2)
            return Get(connection);
        return "NOT SUCCESSFUL\n" + connection.getResponseCode() + " " + connection.getResponseMessage();
    }

    /**
     * this method prints a user guide for the user for how to use the application
     */
    private void PrintHelp() {
        System.out.println("--url: [Address] := address as the request url");
        System.out.println("-M(--method): (GET, POST, PATCH, DELETE, PUT) := request method type");
        System.out.println("-i: tells to print the response headers.");
        System.out.println("-H(--headers): key:value&... := setting the headers of the request");
        System.out.println("-h(--help)  will print the help");
        System.out.println("-f := if typed it will allow the following redirection");
        System.out.println("--save [folder name] := will save the request for you");
        System.out.println("--data [message body] := is the preferred message body with formData");
        System.out.println("--json [message body] := is the preferred message body with Json");
        System.out.println("note that in each request you should use one of json and data");
        System.out.println("--upload [URI] := will upload a request from a URI in system, URI is a absolute path.");
        System.out.println("output [empty or name] := will save the response body in a txt file with name : output [name or current date]");
        System.out.println("--type := encoded is for urlencoded, formData is for FormData, binary is for uploading a binary upload, json is for json.");
        System.out.println("note that for encoded and formData the data should be in \"a=b&c=d\" and for json it should be like : {a:b,c:d} also for binary upload use:" +
                "--uploadBinary [address] to send it.");
    }
}