import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

enum requests {
    PATCH, PUT, POST, DELETE, GET;
}

public class View extends JFrame {
    private static final int NUMBER_OF_MENUS = 3;
    private static final int NUMBER_OF_SUBMENUS = 2;
    private final Options options;
    private static int[] numberOfHeaders = {1, 1, 1};
    private boolean fullScreen = false;
    private JMenuBar menuBar;
    private ArrayList<JMenu> menus;
    private ArrayList<ArrayList<JMenuItem>> submenus;
    private JPanel center, left, right;
    private JTabbedPane tabbedPane;
    private JTextField urlTextField;
    private JTextArea textArea, jTextArea, jTextArea1;
    private JButton saveURL;
    private static ArrayList<JPanel> centerPanels, rightPanels, rightBodyPanels;
    private static JPanel formDataPanel, jSONpanel, binaryDataPanel;
    private JList list;
    private myFocus focus = new myFocus();

    private static class myFocus extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField jTextField = (JTextField)e.getSource();
            jTextField.setText("");
            if (jTextField.getName().equals("New Header"))
                return;
            for (int j = 0; j < 5; j++) {
                if (numberOfHeaders[0] > 19)
                    break;
                centerPanels.get(0).getComponent(numberOfHeaders[0] * 5 + j).setVisible(true);
                centerPanels.get(2).getComponent(numberOfHeaders[1] * 5 + j).setVisible(true);
                formDataPanel.getComponent(numberOfHeaders[2] * 5 + j).setVisible(true);
            }
            for (int i = 0; i < 3; i++)
                numberOfHeaders[i]++;
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField jTextField = (JTextField)e.getSource();
            if (jTextField.getText().trim().equals(""))
                jTextField.setText(jTextField.getName());
        }
    }

    View(Options options) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        this.options = options;
        setTitle("Farshid Nooshi Midterm project-term2(98-99)");
//        setAlwaysOnTop(true);
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
        center.setLayout(new BorderLayout());
        JComboBox<requests> comboBox = new JComboBox<>(requests.values());
        comboBox.setPreferredSize(new Dimension(70, 55));
        JPanel temporary = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        urlTextField.setPreferredSize(new Dimension(200, 55));
        saveURL.setPreferredSize(new Dimension(70, 55));
        temporary.add(comboBox);
        temporary.add(urlTextField);
        temporary.add(saveURL);
        center.add(temporary, BorderLayout.NORTH);
        center.add(tabbedPane, BorderLayout.CENTER);
        initTabs();
    }

    private void initRightPanel() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        right.setLayout(new BorderLayout(5, 5));
        JPanel help = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        createSpecificLableRightPanel("200 OK", new Color(83, 255, 119), help);
        createSpecificLableRightPanel("1.03 S", new Color(10, 84, 255), help);
        createSpecificLableRightPanel("11.6 KB", new Color(10, 84, 255), help);
        right.add(help, BorderLayout.NORTH);
        rightPanels = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            rightPanels.add(new JPanel());
        rightPanels.get(0).setName("Body");
        rightPanels.get(1).setName("Header");
        for (JPanel panel : rightPanels)
            jTabbedPane.add(panel.getName(), panel);
        right.add(jTabbedPane, BorderLayout.CENTER);
//**********************************************************************************************************************
        JPanel reference = rightPanels.get(1);// HEADER
        reference.setLayout(new GridLayout(16, 3, 5, 5));
        for (int i = 0; i < 15; i++) {
            reference.add(new JLabel(new ImageIcon("OrbitProject/menu_32px.png")));
            JTextField textField = new JTextField(), textField1 = new JTextField();
            reference.add(textField);
            reference.add(textField1);
            textField.setEditable(false);
            textField1.setEditable(false);
        }
        JButton copy = new JButton("Copy to Clipboard");
        reference.add(copy);
//**********************************************************************************************************************
        reference = rightPanels.get(0);//Body
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        textArea = new JTextArea("TO DO IN THE NEXT PHASES");
        jTextArea = new JTextArea("TO DO IN THE NEXT PHASES");
        jTextArea1 = new JTextArea("TO DO IN THE NEXT PHASES");
        reference.setLayout(new BorderLayout());
        reference.add(jTabbedPane1, BorderLayout.CENTER);
        rightBodyPanels = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            rightBodyPanels.add(new JPanel(new BorderLayout())); // default in bashe ta too phase 2&3 avaz beshe
        rightBodyPanels.get(0).setName("Raw");
        rightBodyPanels.get(1).setName("Preview");
        rightBodyPanels.get(2).setName("JSON");
        rightBodyPanels.get(0).add(textArea, BorderLayout.CENTER);
        rightBodyPanels.get(1).add(jTextArea1, BorderLayout.CENTER);
        rightBodyPanels.get(2).add(jTextArea, BorderLayout.CENTER);
        textArea.setEditable(false);
        jTextArea.setEditable(false);
        jTextArea1.setEditable(false);
        for (JPanel panel : rightBodyPanels)
            jTabbedPane1.add(panel.getName(), panel);
    }

    private void createSpecificLableRightPanel(String text, Color color, JPanel help) {
        JLabel memory = new JLabel(text);
        memory.setBackground(color);
        memory.setForeground(Color.WHITE);
        memory.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        memory.setOpaque(true);
        help.add(memory);
    }

    private void initLeftPanel() {
        ArrayList<String> folders = new ArrayList<>();
        folders.add("Requests");
        list = new JList(folders.toArray());
        JButton createNode = new JButton("create");
        left.setLayout(new BorderLayout());
        JLabel label = new JLabel("Insomnia", new ImageIcon("OrbitProject/res/Insomnia.png"), SwingConstants.LEFT);
        label.setBackground(new Color(182, 1, 255));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        left.add(label, BorderLayout.NORTH);
        left.add(createNode, BorderLayout.SOUTH);
        left.add(list);

        createNode.addActionListener(e -> {
            String str = JOptionPane.showInputDialog(null, "Type a name", JOptionPane.INPUT_VALUE_PROPERTY);
            list.clearSelection();
            folders.add(str);
            list.setListData(folders.toArray());
        });
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
        submenus.get(0).get(0).addActionListener(e -> {
            options.showGUI();
        });
        submenus.get(0).get(1).addActionListener(e -> {
            dispose();
//            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        submenus.get(1).get(0).addActionListener(e -> {
            fullScreen = !fullScreen;
            if (fullScreen)
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            else
                setExtendedState(JFrame.NORMAL);
        });
        submenus.get(1).get(1).addActionListener(e -> {
            if (left.isVisible())
                left.setVisible(false);
            else
                left.setVisible(true);
        });
        submenus.get(2).get(0).addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Farshid Nooshi\nStudent ID: 9831068\nEmail: FarshidNooshi726@aut.ac.ir", "About", JOptionPane.INFORMATION_MESSAGE);
        });
        submenus.get(2).get(1).addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "To Do for next phases", "Help", JOptionPane.INFORMATION_MESSAGE);

        });
    }

    void setToDark() {
        System.out.println("seen");
    }

    void setToLight() {
        System.out.println("!seen");
    }

    private void initTabs() {
        centerPanels = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            centerPanels.add(new JPanel());
        centerPanels.get(0).setName("Header");
        centerPanels.get(1).setName("Auth");
        centerPanels.get(2).setName("Query");
        centerPanels.get(3).setName("Body");
        for (JPanel j : centerPanels)
            tabbedPane.add(j.getName(), j);
//**********************************************************************************************************************
        JPanel reference = centerPanels.get(0);// HEADER
        reference.setLayout(new GridLayout(20, 5, 5, 5));
        for (int i = 0; i < 20; i++) {
            reference.add(new JLabel(new ImageIcon("OrbitProject/menu_32px.png")));
            JTextField tt = new JTextField("New Header"), tt1 = new JTextField("New Value");
            tt.setName("New Header");
            tt.addFocusListener(focus);
            tt1.setName(tt1.getText());
            tt1.addFocusListener(focus);
            reference.add(tt);
            reference.add(tt1);
            reference.add(new JCheckBox());
            reference.add(new JButton(new ImageIcon("OrbitProject/waste_32px.png")));
        }
        for (int i = numberOfHeaders[0]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                reference.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        reference = centerPanels.get(2);
        reference.setLayout(new GridLayout(20, 5, 5, 5));
        for (int i = 0; i < 20; i++) {
            reference.add(new JLabel(new ImageIcon("OrbitProject/menu_32px.png")));
            JTextField tt = new JTextField("New Header"), tt1 = new JTextField("New Value");
            tt.setName("New Header");
            tt.addFocusListener(focus);
            tt1.setName(tt1.getText());
            tt1.addFocusListener(focus);
            reference.add(tt);
            reference.add(tt1);
            reference.add(new JCheckBox());
            reference.add(new JButton(new ImageIcon("OrbitProject/waste_32px.png")));
        }
        for (int i = numberOfHeaders[1]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                reference.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        reference = centerPanels.get(1);
        reference.setLayout(new GridLayout(5, 2));
        JTextField token = new JTextField();
        JTextField prefix = new JTextField();
        JCheckBox isEnabled = new JCheckBox();
        JLabel token1 = new JLabel("TOKEN");
        JLabel prefix1 = new JLabel("PREFIX");
        JLabel enabled = new JLabel("ENABLED");
        reference.add(token1);
        reference.add(token);
        reference.add(prefix1);
        reference.add(prefix);
        reference.add(enabled);
        reference.add(isEnabled);
//**********************************************************************************************************************
        reference = centerPanels.get(3);
        reference.setLayout(new BorderLayout());
        initBody();
    }

    private void initBody() {
        JPanel reference = centerPanels.get(3);
        binaryDataPanel = new JPanel();
        jSONpanel = new JPanel();
        formDataPanel = new JPanel();
        jSONpanel.setName("jSONpanel");
        formDataPanel.setName("formDataPanel");
        binaryDataPanel.setName("binaryDataPanel");
        JTabbedPane help = new JTabbedPane();
        help.add(jSONpanel.getName(), jSONpanel);
        help.add(formDataPanel.getName(), formDataPanel);
        help.add(binaryDataPanel.getName(), binaryDataPanel);
        reference.add(help, BorderLayout.CENTER);
//**********************************************************************************************************************
        binaryDataPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("SELECTED FILE");
        binaryDataPanel.add(label, BorderLayout.NORTH);
        JTextField ttt = new JTextField("...");
        ttt.setEditable(false);
        binaryDataPanel.add(ttt, BorderLayout.CENTER);
        JButton selectItem = new JButton("Choose File");
        JButton resetItem = new JButton("Reset File");
        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(resetItem);
        tmp.add(selectItem);
        binaryDataPanel.add(tmp, BorderLayout.SOUTH);
//**********************************************************************************************************************
        jSONpanel.setLayout(new BorderLayout());
        JTextField textField1 = new JTextField("...");
        textField1.setName("...");
        textField1.addFocusListener(focus);
        jSONpanel.add(textField1, BorderLayout.CENTER);
//**********************************************************************************************************************
        formDataPanel.setLayout(new GridLayout(20, 5, 5, 5));
        for (int i = 0; i < 20; i++) {
            formDataPanel.add(new JLabel(new ImageIcon("OrbitProject/menu_32px.png")));
            JTextField tt = new JTextField("New Header"), tt1 = new JTextField("New Value");
            tt.setName("New Header");
            tt.addFocusListener(focus);
            tt1.setName(tt1.getText());
            tt1.addFocusListener(focus);
            formDataPanel.add(tt);
            formDataPanel.add(tt1);
            formDataPanel.add(new JCheckBox());
            formDataPanel.add(new JButton(new ImageIcon("OrbitProject/waste_32px.png")));
        }
        for (int i = numberOfHeaders[2]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                formDataPanel.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        binaryDataPanel.setVisible(false);
        jSONpanel.setVisible(false);
        formDataPanel.setVisible(false);
    }

}
