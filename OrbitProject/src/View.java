// In The Name Of GOD

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

enum requests {
    PATCH, PUT, POST, DELETE, GET
}

public class View extends JFrame {
    private static final int NUMBER_OF_MENUS = 3; // number of menus of the program
    private static final int NUMBER_OF_SUBMENUS = 2; // its the number of submenus of each menu
    private static int[] numberOfHeaders = {1, 1, 1}; // an index holder for the headers that are visible at the moment
    private static ArrayList<JPanel> centerPanels, rightPanels, rightBodyPanels; // three parts of the screen subpanels
    private static JPanel formDataPanel, jSONpanel, binaryDataPanel; // three panels for formData, Json, binaryData in the program
    private final Options options; // options panel that should be opened in another frame
    private boolean fullScreen = false; // say if it's in the fullscreen mode or not
    private JMenuBar menuBar; // for the menu of the frame
    private ArrayList<JMenu> menus; // menus of the program
    private ArrayList<ArrayList<JMenuItem>> submenus; // submenus of the program
    private JPanel center, left, right; // three panles for left center and right side of the program
    private JTabbedPane tabbedPane; // used for center panel (4 possibilities)
    private JTextField urlTextField; // url textField for writing a url
    private JTextArea textArea, jTextArea, jTextArea1; // textAreas for right panel but they'll be changed
    private JButton saveurl; // a button for saving the url
    private JList list; // for making folders in the left panel
    private myFocus focus = new myFocus(); // it's a focus listener

    /**
     * the constructor of the frame that build the things
     *
     * @param options is the option frame
     * @throws ClassNotFoundException          if the class didn't exist
     * @throws UnsupportedLookAndFeelException if the look and feel wasn't available in the system
     * @throws InstantiationException
     * @throws IllegalAccessException          if we wanted an illegal access to something in the system
     */
    View(Options options) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        this.options = options;
        setTitle("Farshid Nooshi Midterm project-term2(98-99)");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setLayout(new BorderLayout());
        center = new JPanel();
        left = new JPanel();
        right = new JPanel();
        menuBar = new JMenuBar();
        menus = new ArrayList<>();
        submenus = new ArrayList<>();
        urlTextField = new JTextField();
        saveurl = new JButton("Send");
        saveurl.addActionListener(e -> {
            System.out.println("Send URL got an action event.");
        });
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

    /**
     * this method initializes the center panel for our GUI
     */
    private void initCenterPanel() {
        center.setLayout(new BorderLayout());
        JComboBox<requests> comboBox = new JComboBox<>(requests.values());
        comboBox.setPreferredSize(new Dimension(70, 55));
        JPanel temporary = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        urlTextField.setPreferredSize(new Dimension(200, 55));
        saveurl.setPreferredSize(new Dimension(70, 55));
        temporary.add(comboBox);
        temporary.add(urlTextField);
        temporary.add(saveurl);
        center.add(temporary, BorderLayout.NORTH);
        center.add(tabbedPane, BorderLayout.CENTER);
        initTabs();
    }

    /**
     * this methos initializes the right panel for our GUI
     * also i separeted parts of the code for better readability
     * each separated part is used for one panel
     */
    private void initRightPanel() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        right.setLayout(new BorderLayout(5, 5));
        JPanel help = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        try {
            createSpecificLableRightPanel("200 OK", new Color(83, 255, 119), help);
            createSpecificLableRightPanel("1.03 S", new Color(10, 84, 255), help);
            createSpecificLableRightPanel("11.6 KB", new Color(10, 84, 255), help);
        } catch (Exception e) {
            e.printStackTrace();
            dispose();
        }
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
        copy.addActionListener(e -> {
            System.out.println("copy button got action event.");
        });
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

    /**
     * @param text  is a text string
     * @param color is the color of the label
     * @param help  is a panel to adding to
     * @throws NullPointerException checking for not being null in help
     */
    private void createSpecificLableRightPanel(String text, Color color, JPanel help) throws NullPointerException {
        JLabel memory = new JLabel(text);
        memory.setBackground(color);
        memory.setForeground(Color.WHITE);
        memory.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        memory.setOpaque(true);
        help.add(memory);
    }

    /**
     * this method initializes the left panel of the program
     * we have a list for creating folders
     */
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

    /**
     * showing the frame in a window
     */
    void showGUI() {
        pack();
        setVisible(true);
    }

    /**
     * initializes the menu bar of our program
     * it's extendable also for adding multiple futures to the program
     */
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

    /**
     * it'll be done in the next phases it's purpose is to set the frame to dark mode!
     */
    void setToDark() {
        System.out.println("seen");
    }

    /**
     * the purpose of this method is to setting back everything to it's default mode
     * i mean putting back the changes that setToDark() has made.
     */
    void setToLight() {
        System.out.println("!seen");
    }

    /**
     * initializes the tabes of teh center panel
     * i separated the parts of the code for better understanding
     */
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
            JCheckBox checkBoxx = new JCheckBox();
            checkBoxx.addActionListener(e -> {
                System.out.println("check box clicked. and state of this box changed.");
            });
            reference.add(checkBoxx);
            JButton jButton = new JButton(new ImageIcon("OrbitProject/waste_32px.png"));
            reference.add(jButton);
            jButton.addActionListener(e -> {
                System.out.println("a header delete button clicked.");
            });
        }
        for (int i = numberOfHeaders[0]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                reference.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        reference = centerPanels.get(2);//Query
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
            JCheckBox checkBoxx = new JCheckBox();
            checkBoxx.addActionListener(e -> {
                System.out.println("check box clicked. and state of this box changed.");
            });
            reference.add(checkBoxx);
            JButton jButton = new JButton(new ImageIcon("OrbitProject/waste_32px.png"));
            reference.add(jButton);
            jButton.addActionListener(e -> {
                System.out.println("a header delete button clicked.");
            });
        }
        for (int i = numberOfHeaders[1]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                reference.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        reference = centerPanels.get(1);// Auth
        reference.setLayout(new GridLayout(5, 2));
        JTextField token = new JTextField();
        JTextField prefix = new JTextField();
        JCheckBox isEnabled = new JCheckBox();
        JLabel token1 = new JLabel("TOKEN");
        JLabel prefix1 = new JLabel("PREFIX");
        JLabel enabled = new JLabel("ENABLED");
        isEnabled.addActionListener(e -> {
            System.out.println("Auth checkbox got action event.");
        });
        reference.add(token1);
        reference.add(token);
        reference.add(prefix1);
        reference.add(prefix);
        reference.add(enabled);
        reference.add(isEnabled);
//**********************************************************************************************************************
        reference = centerPanels.get(3);// Body
        reference.setLayout(new BorderLayout());
        initBody();
    }

    /**
     * because of the body of the center panel is too long i separated the code to another method for dividing it to improving the readability
     */
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
        binaryDataPanel.setLayout(new BorderLayout()); // Binary Data
        JLabel label = new JLabel("SELECTED FILE");
        binaryDataPanel.add(label, BorderLayout.NORTH);
        JTextField ttt = new JTextField("...");
        ttt.setEditable(false);
        binaryDataPanel.add(ttt, BorderLayout.CENTER);
        JButton selectItem = new JButton("Choose File");
        JButton resetItem = new JButton("Reset File");
        selectItem.addActionListener(e -> {
            System.out.println("choose a file button got an action event.");
        });
        resetItem.addActionListener(e -> {
            System.out.println("reset Item button got an action event.");
        });
        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(resetItem);
        tmp.add(selectItem);
        binaryDataPanel.add(tmp, BorderLayout.SOUTH);
//**********************************************************************************************************************
        jSONpanel.setLayout(new BorderLayout());// JSON panel
        JTextField textField1 = new JTextField("...");
        textField1.setName("...");
        textField1.addFocusListener(focus);
        jSONpanel.add(textField1, BorderLayout.CENTER);
//**********************************************************************************************************************
        formDataPanel.setLayout(new GridLayout(20, 5, 5, 5)); // Form Data
        for (int i = 0; i < 20; i++) {
            formDataPanel.add(new JLabel(new ImageIcon("OrbitProject/menu_32px.png")));
            JTextField tt = new JTextField("New Header"), tt1 = new JTextField("New Value");
            tt.setName("New Header");
            tt.addFocusListener(focus);
            tt1.setName(tt1.getText());
            tt1.addFocusListener(focus);
            formDataPanel.add(tt);
            formDataPanel.add(tt1);
            JCheckBox checkBoxx = new JCheckBox();
            checkBoxx.addActionListener(e -> {
                System.out.println("check box clicked. and state of this box changed.");
            });
            formDataPanel.add(checkBoxx);
            JButton jButton = new JButton(new ImageIcon("OrbitProject/waste_32px.png"));
            formDataPanel.add(jButton);
            jButton.addActionListener(e -> {
                System.out.println("a header delete button clicked.");
            });
        }
        for (int i = numberOfHeaders[2]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                formDataPanel.getComponent(i * 5 + j).setVisible(false);
//**********************************************************************************************************************
        binaryDataPanel.setVisible(false);
        jSONpanel.setVisible(false);
        formDataPanel.setVisible(false);
    }

    /**
     * this class is built for listening to focuses in the program for textAreas
     */
    private static class myFocus extends FocusAdapter {
        /**
         * clearing the initial text of the textArea is the responsibility
         * and opening a new row of textFields
         *
         * @param e is the focus even
         */
        @Override
        public void focusGained(FocusEvent e) {
            JTextField jTextField = (JTextField) e.getSource();
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

        /**
         * setting back the text to its initial text is the responsibility
         *
         * @param e is focus Event
         */
        @Override
        public void focusLost(FocusEvent e) {
            JTextField jTextField = (JTextField) e.getSource();
            if (jTextField.getText().trim().equals(""))
                jTextField.setText(jTextField.getName());
        }
    }

}
