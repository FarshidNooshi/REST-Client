import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("project started");
        try {
            Options options = new Options();
            View view = new View(options);
            options.setView(view);
            view.showGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
