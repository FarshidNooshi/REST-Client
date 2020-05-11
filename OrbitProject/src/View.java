import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class View extends JFrame {
    private static final int NUMBER_OF_MENUS = 3;
    private static final int NUMBER_OF_SUBMENUS = 2;
    private JMenuBar menuBar;
    private ArrayList<JMenu> menus;
    private ArrayList<ArrayList<JMenuItem>> submenus;

    public View() {
        menuBar = new JMenuBar();
        menus = new ArrayList<>();
        submenus = new ArrayList<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenuBar();
        setJMenuBar(menuBar);

    }

    public void showGUI() {
        pack();
        setVisible(true);
    }

    private void initMenuBar() { // Action listener should add
        menus.add(new JMenu("Application"));
        menus.add(new JMenu("View"));
        menus.add(new JMenu("Help"));
        menus.get(0).setMnemonic(KeyEvent.VK_ASTERISK);
        menus.get(0).setMnemonic(KeyEvent.VK_1);
        menus.get(0).setMnemonic(KeyEvent.VK_2);
        menus.get(0).setMnemonic(KeyEvent.VK_3);
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            menuBar.add(menus.get(i));
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            submenus.add(new ArrayList<>());
        submenus.get(0).add(new JMenuItem("Options", KeyEvent.VK_O));
        submenus.get(0).add(new JMenuItem("Exit", KeyEvent.VK_E));
        submenus.get(1).add(new JMenuItem("Toggle Full Screen", KeyEvent.VK_T));
        submenus.get(1).add(new JMenuItem("Toggle sidebar", KeyEvent.VK_S));
        submenus.get(2).add(new JMenuItem("About", KeyEvent.VK_A));
        submenus.get(2).add(new JMenuItem("Help", KeyEvent.VK_H));
        submenus.get(0).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        submenus.get(0).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        submenus.get(1).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        submenus.get(1).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        submenus.get(2).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        submenus.get(2).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            for (int j = 0; j < NUMBER_OF_SUBMENUS; j++) {
                menus.get(i).add(submenus.get(i).get(j));
                if (j == 0)
                    menus.get(i).addSeparator();
            }
    }

}
