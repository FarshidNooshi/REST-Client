package Phase2;

import java.util.ArrayList;

public class Main {
    private static Request request;
    /**
     * //--url, -M(--method), -H(--headers), -i, -h(--help), -f, -O(--output), -S(--save), -d(--data), -j(--json),
     * // --upload, list
     * @param args is the inputs of this HTTPCLIENT service
     */
    public static void main(String[] args){
        if (args.length == 0)
            new Exception("INVALID INPUT").printStackTrace();
        request = new Request();
        ArrayList<Pair<String, Boolean>> arrayList = new ArrayList<>();
        init(arrayList);
        initArgs(args);
        if (args[0].equalsIgnoreCase("list")) {

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
                                    if (args[i + 1].charAt(0) == '-')
                                        continue;
                                    request.getMp().replace(element.getFirst(), args[i + 1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            if (element.getSecond()) {
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
        }
    }

    /**
     * this method initializes the arrayList for checking for the next input in the commands or not
     * @param arr says if each command needs a second value or not
     */
    private static void init(ArrayList<Pair<String, Boolean>> arr) {
        arr.add(new Pair<>("url", true));
        arr.add(new Pair<>("method", true));
        arr.add(new Pair<>("headers", true));
        arr.add(new Pair<>("i", false));
        arr.add(new Pair<>("help", false));
        arr.add(new Pair<>("f", false));
        arr.add(new Pair<>("save", false));
        arr.add(new Pair<>("data", true));
        arr.add(new Pair<>("json", true));
        arr.add(new Pair<>("upload", true));
        arr.add(new Pair<>("output", true));
    }

    /**
     * this method replaces -* commands with their --* equivalent
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
