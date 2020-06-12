package Phase1;// In The Name Of GOD

/**
 * I followed the concepts of MVC designing pattern
 */
public class Main {

    private static View view;

    /**
     * the main method and the main class of the program
     *
     * @param args are the inputs of the program.
     */
    public static void main(String[] args) {
        System.out.println("project started");
        try {
            Options options = new Options();
            view = new View(options);
            options.setView(view);
            view.showGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View getView() {
        return view;
    }
}

