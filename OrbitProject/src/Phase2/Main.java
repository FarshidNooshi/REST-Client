package Phase2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    private static Request request;

    /**
     * //--url, -M(--method), -H(--headers), -i, -h(--help), -f, -O(--output), -S(--save), -d(--data), -j(--json),
     * // --upload, list
     *
     * @param args is the inputs of this HTTPCLIENT service
     */
    public static void main(String[] args) throws IOException {
        String basePath = new File("").getAbsolutePath();
        if (args.length == 0)
            new Exception("INVALID INPUT").printStackTrace();
        request = new Request();
        ArrayList<Pair<String, Boolean>> arrayList = new ArrayList<>();
        init(arrayList);
        initArgs(args);
        if (args[0].equalsIgnoreCase("fire")) {
            File dir = new File(basePath + File.separator + args[1]);
            Reader tmp = new Reader(dir.getAbsolutePath() + File.separator + args[2]);
            request = (Request) tmp.ReadFromFile();
            tmp.close();
            HTTpService service = new HTTpService(request);
            try {
                service.runService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            File fileToCreate = new File(basePath + File.separator + args[1]);
            if (fileToCreate.mkdir())
                System.out.println("directory created!");
            else
                System.err.println("Couldn't create the directory");
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                File file = new File(basePath);
                String[] arr = file.list();
                assert arr != null;
                for (String temp : arr)
                    if (!temp.contains("Phase") && !temp.equals("OutputFolder"))
                        System.out.println(temp);
            } else {
                Path path = Paths.get(basePath + File.separator + args[1]);
                File file = new File(path.toString());
                if (file.exists()) {
                    String[] arr = file.list();
                    assert arr != null;
                    for (String temp : arr) {
                        System.out.print(temp + ". ");
                        Reader reader = new Reader(path.toString() + File.separator + temp);
                        Request req = (Request) reader.ReadFromFile();
                        reader.close();
                        System.out.print("URL: " + req.getMp().get("url") + " | ");
                        System.out.print("method: " + req.getMp().get("method") + " | ");
                        System.out.print("headers: " + req.getMp().get("header") + " | ");
                        System.out.println("data: " + req.getMp().get("data"));
                    }
                } else
                    System.err.println("error occurred while opening the directory.");
            }
        } else {
            for (Pair<String, Boolean> element : arrayList) {
                if (element.getFirst().equals("i") || element.getFirst().equals("f")) {
                    for (String temp : args)
                        if (temp.equals("-" + element.getFirst())) {
                            request.getMp().replace(element.getFirst(), "true");
                            break;
                        }
                } else {
                    for (int i = 0; i < args.length; i++) {
                        String relax = args[i].substring(2);
                        if (relax.equals(element.getFirst())) {
                            if (element.getFirst().equals("output")) {
                                try {
                                    if ((args.length > i + 1 && args[i + 1].charAt(0) == '-') || (args.length == i + 1)) {
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd,HH.mm.ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        request.getMp().replace("output", "output[" + dtf.format(now) + "]" + ".txt");
                                    } else if (args.length > i + 1)
                                        request.getMp().replace(element.getFirst(), args[i + 1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (element.getSecond()) {
                                try {
                                    request.getMp().replace(element.getFirst(), args[i + 1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                request.getMp().replace(element.getFirst(), "true");
                            }
                        }
                    }
                }
            }
            if (!request.getMp().get("upload").equals("")) {
//                Reader tmp = new Reader(request.getMp().get("upload"));// TODO check beshe
//                request = (Request) tmp.ReadFromFile();
//                tmp.close();
//                System.out.println("Request renewed.");
                request.getMp().replace("uploadBinary", request.getMp().get("upload"));
            }
            HTTpService service = new HTTpService(request);
            if (!request.getMp().get("save").equals("false"))
                SaveRequest();
            try {
                service.runService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method will save the request to the path that user gave.
     *
     * @throws IOException if the exception occurred
     */
    private static void SaveRequest() throws IOException {
        String basePath = new File("").getAbsolutePath();
        Path path = Paths.get(basePath + File.separator + request.getMp().get("save"));
        String name = (Objects.requireNonNull(new File(path.toString()).list()).length + 1) + ".AUT";
        Writer writer = new Writer(path.toString() + File.separator + name);
        writer.WriteToFile(request);
    }

    /**
     * this method initializes the arrayList for checking for the next input in the commands or not
     *
     * @param arr says if each command needs a second value or not
     */
    private static void init(ArrayList<Pair<String, Boolean>> arr) {
        arr.add(new Pair<>("url", true));//done
        arr.add(new Pair<>("method", true));//POST:done, GET:done, PATCH: , DELETE:done, PUT:done
        arr.add(new Pair<>("headers", true));//done
        arr.add(new Pair<>("i", false));//done
        arr.add(new Pair<>("help", false));//done
        arr.add(new Pair<>("f", false));// done
        arr.add(new Pair<>("save", true));//done
        arr.add(new Pair<>("data", true));//done
        arr.add(new Pair<>("json", true));//done
        arr.add(new Pair<>("upload", true));//done
        arr.add(new Pair<>("output", true));//done
        arr.add(new Pair<>("type", true));//done
        arr.add(new Pair<>("uploadBinary", true));//done
    }

    /**
     * this method replaces -* commands with their --* equivalent
     *
     * @param args is the input commands
     */
    private static void initArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String temp = args[i];
            switch (temp) {
                case "-M":
                    args[i] = "--method";
                    break;
                case "-H":
                    args[i] = "--headers";
                    break;
                case "-h":
                    args[i] = "--help";
                    break;
                case "-O":
                    args[i] = "--output";
                    break;
                case "-S":
                    args[i] = "--save";
                    break;
                case "-d":
                    args[i] = "--data";
                    break;
                case "-j":
                    args[i] = "--json";
                    break;
            }
        }
    }
}
