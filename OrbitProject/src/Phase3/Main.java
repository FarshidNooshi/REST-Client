package Phase3;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Phase1.Main.main(args);
//        Phase2.Main.main(new String[]{"list"});
        Controller controller = new Controller(Phase1.Main.getView());
    }
}
