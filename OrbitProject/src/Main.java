// In The Name Of GOD

/**
 * I followed the concepts of MVC designing pattern
 */
public class Main {
    /**
     * the main method and the main class of the program
     *
     * @param args ...
     */
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
