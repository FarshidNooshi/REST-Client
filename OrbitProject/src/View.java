import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

enum requests {
    PATCH, PUT, POST, DELETE, GET;
}

public class View extends JFrame {
    private static final int NUMBER_OF_MENUS = 3;
    private static final int NUMBER_OF_SUBMENUS = 2;
    private JMenuBar menuBar;
    private ArrayList<JMenu> menus;
    private ArrayList<ArrayList<JMenuItem>> submenus;
    private JPanel center, left, right;
    private JTabbedPane tabbedPane;
    private ArrayList<Panel> centerPanels;
    private JTextField urlTextField;
    private JComboBox comboBox;
    private JButton saveURL;

    public View() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setTitle("Farshid Nooshi Midterm project-term2(98-99)");
        setAlwaysOnTop(true);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setLayout(new BorderLayout());
        center = new JPanel();
        left = new JPanel();
        right = new JPanel();
        menuBar = new JMenuBar();
        menus = new ArrayList<>();
        submenus = new ArrayList<>();
        urlTextField = new JTextField();
        saveURL = new JButton("Send");
        tabbedPane = new JTabbedPane();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenuBar();
        setJMenuBar(menuBar);
        add(center, BorderLayout.CENTER);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
    }

    private void initCenterPanel() {
        ArrayList<String> tmp = new ArrayList<>();
        for (requests r : requests.values())
            tmp.add(r.name());
        comboBox = new JComboBox(tmp.toArray());
        comboBox.setPreferredSize(new Dimension(70, 60));
        center.setLayout(new BorderLayout());
        JPanel temporary = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        urlTextField.setPreferredSize(new Dimension(200, 60));
        saveURL.setPreferredSize(new Dimension(70, 60));
        temporary.add(comboBox);
        temporary.add(urlTextField);
        temporary.add(saveURL);
        center.add(temporary, BorderLayout.NORTH);
        center.add(tabbedPane, BorderLayout.CENTER);

    }

    private void initRightPanel() {

    }

    private void initLeftPanel() {
        left.setLayout(new BorderLayout());
        JLabel label = new JLabel("Insomnia", new ImageIcon("OrbitProject/res/Insomnia.png"), SwingConstants.LEFT);
        label.setBackground(new Color(182, 1, 255));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        left.add(label, BorderLayout.NORTH);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Request");
    }

    void showGUI() {
        pack();
        setVisible(true);
    }

    private void initMenuBar() { // Action listener should add
        setIconImage(new ImageIcon("OrbitProject/res/Insomnia.png").getImage());
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
