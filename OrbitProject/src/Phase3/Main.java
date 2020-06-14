package Phase3;

public class Main {
    public static void main(String[] args) {
        Phase1.Main.main(args);
        Controller controller = new Controller(Phase1.Main.getView());
        controller.establish();
    }
}
