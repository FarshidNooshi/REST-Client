import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.out.println("project started");
        try {
            View view = new View();
            view.showGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
