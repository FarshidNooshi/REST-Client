package Phase3;

import Phase1.View;
import Phase2.Reader;
import Phase2.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Controller {
    ActionListener actionListener;
    private View view;
    private Request request;

    public Controller(View view) {
        request = new Request();
        this.view = view;
        actionListener = new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JButton) e.getSource()).setEnabled(false);
                view.getUrlTextField().setEnabled(false);
                Worker worker = new Worker();
                worker.execute();
            }
        };
    }

    public void establish() {
        initLeftListeners();
        addTrashListener(View.getCenterPanels().get(0));
        addTrashListener(View.getCenterPanels().get(2));
        addTrashListener(View.getFormDataPanel());
        initBinaryBody();
        initBodyListeners();
        view.getSaveURL().addActionListener(actionListener);
    }

    private void initBodyListeners() {
        JTabbedPane bodyTabbedPane = view.getBodyTabbedPane();
        bodyTabbedPane.addChangeListener(e -> {
            if (bodyTabbedPane.getSelectedComponent().getName().equalsIgnoreCase("formData")) {
                String str = JOptionPane.showInputDialog(view, "Type encoded if you want to send urlEncoded or formData otherwise", JOptionPane.INPUT_VALUE_PROPERTY);
                request.getMp().replace("type", str);
            }
        });
    }

    private void initBinaryBody() {
        JButton choose = view.getSelectItem();
        choose.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(view);
            fileChooser.setMultiSelectionEnabled(false);
            JLabel label = view.getFileSelected();
            label.setText("File selected: " + fileChooser.getSelectedFile().getName());
            label.setIcon(new ImageIcon("OrbitProject/Data/save_close_100px.png"));
            request.getMp().replace("uploadBinary", fileChooser.getSelectedFile().getAbsolutePath());
        });
        JButton reset = view.getResetItem();
        reset.addActionListener(e -> {
            JLabel label = view.getFileSelected();
            label.setText("File selected: " + "Nothing");
            label.setIcon(new ImageIcon("OrbitProject/Data/file_100px.png"));
        });
    }

    private void addTrashListener(JPanel panel) {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            JButton trash = (JButton) panel.getComponent(finalI * 5 + 4);
            trash.addActionListener(e -> {
                JTextField header = (JTextField) panel.getComponent(finalI * 5 + 1);
                JTextField value = (JTextField) panel.getComponent(finalI * 5 + 2);
                JCheckBox checkBox = (JCheckBox) panel.getComponent(finalI * 5 + 3);
                checkBox.setSelected(false);
                header.setText(header.getName());
                value.setText(value.getName());
            });
        }
    }

    private void initLeftListeners() {
        JButton createNode = view.getCreateNode();
        JList<Object> list = view.getList();
        ArrayList<String> folders = view.getFolders();
        createNode.addActionListener(e -> {
            String str = JOptionPane.showInputDialog(null, "Type a name", JOptionPane.INPUT_VALUE_PROPERTY);
            File fileToCreate = new File("OrbitProject\\src" + File.separator + str);
            if (fileToCreate.mkdir()) {
                folders.add(folders.size(), str);
                System.out.println("directory created!");
            } else
                System.err.println("Couldn't create the directory");
            list.setListData(folders.toArray());
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    System.out.println(folders.get(index));
                    final JFileChooser fileChooser = new JFileChooser("OrbitProject\\src" + File.separator + folders.get(index));
                    fileChooser.showOpenDialog(view);
                    fileChooser.setMultiSelectionEnabled(false);
                    // TODO request entekhab shode ro ersal kon
                    try {
                        Reader reader = new Reader(fileChooser.getSelectedFile().getAbsolutePath());
                        request = (Request) reader.ReadFromFile();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void executeService(Request request) throws IOException {
        request.SaveRequest();
        String base = (new File("OrbitProject\\src\\OutputFolder").getAbsolutePath());
        String name = "output" + ".txt";
        try (PrintStream writer = new PrintStream(base + File.separator + name)) {
            Phase2.HTTpService service = new Phase2.HTTpService(request, writer);
            service.runService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Worker extends SwingWorker<String, Object> {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * <p>
         * Note that this method is executed only once.
         *
         * <p>
         * Note: this method is executed in a background thread.
         *
         * @return the computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        protected String doInBackground() throws Exception {
            initRequest();
            return null;
        }

        private void initRequest() throws InterruptedException, IOException {
            request.getMp().replace("url", view.getUrlTextField().getText());
            request.getMp().replace("method", Objects.requireNonNull(view.getComboBox().getSelectedItem()).toString());
            {
                StringBuilder builder = new StringBuilder();
                JPanel panel = View.getCenterPanels().get(0);
                for (int i = 0; i < View.getNumberOfHeaders()[0]; i++) {
                    JTextField header = (JTextField) panel.getComponent(i * 5 + 1);
                    JTextField value = (JTextField) panel.getComponent(i * 5 + 2);
                    JCheckBox checkBox = (JCheckBox) panel.getComponent(i * 5 + 3);
                    if (checkBox.isSelected())
                        builder.append(header.getText()).append(":").append(value.getText()).append(";");
                }
                if (builder.length() != 0) {
                    String headers = builder.toString();
                    request.getMp().replace("headers", headers.substring(0, headers.length() - 1));
                }
            }
            request.getMp().replace("i", "true");
            if (view.getOptions().getFollowRedirectBox().isSelected())
                request.getMp().replace("f", "true");
            while (view.getList().getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(view, "choose or create a folder to save your request from the left panel", "Save Error", JOptionPane.ERROR_MESSAGE);
                Thread.sleep(5000);
            }
            String name = view.getList().getSelectedValue().toString();
            File base = new File("OrbitProject\\src").getAbsoluteFile();
            request.getMp().replace("save", base + File.separator + name);
            JTabbedPane bodyTabbedPane = view.getBodyTabbedPane();
            if (bodyTabbedPane.getSelectedComponent().getName().contains("json")) {
                JTextField textField = (JTextField) View.getJsonPanel().getComponents()[0];
                if (textField.getText().length() > 2) {
                    request.getMp().replace("json", textField.getText());
                    request.getMp().replace("type", "json");
                }
            } else if (bodyTabbedPane.getSelectedComponent().getName().contains("form")) {
                StringBuilder builder = new StringBuilder();
                JPanel panel = View.getFormDataPanel();
                for (int i = 0; i < View.getNumberOfHeaders()[2]; i++) {
                    JTextField header = (JTextField) panel.getComponent(i * 5 + 1);
                    JTextField value = (JTextField) panel.getComponent(i * 5 + 2);
                    JCheckBox checkBox = (JCheckBox) panel.getComponent(i * 5 + 3);
                    if (checkBox.isSelected())
                        builder.append(header.getText()).append("=").append(value.getText()).append("&");
                }
                if (builder.length() != 0) {
                    String headers = builder.toString();
                    request.getMp().replace("data", headers);
                }
            } else if (bodyTabbedPane.getSelectedComponent().getName().contains("binary")) {//TODO
                request.getMp().replace("type", "binary");
            }
            executeService(request);
        }

        public void done() {
            view.getSaveURL().setEnabled(true);
            view.getUrlTextField().setEnabled(true);
            request = new Request();
            File base = new File("OrbitProject\\src\\OutputFolder\\output.txt").getAbsoluteFile();
            view.getRaw().setText("");
            try (FileReader fileReader = new FileReader(base.getAbsolutePath());
                 Scanner scanner = new Scanner(fileReader)) {
                initTopRight(scanner, base);
                scanner.nextLine();
                String read;
                while (!(read = scanner.nextLine()).equals("--headers ")) {
                    view.getRaw().append(read + '\n');
                }
                {
                    JPanel panel = View.getRightPanels().get(1);
                    int counter = 0;
                    for (int i = 0; i < 15; i++) {
                        ((JTextArea)panel.getComponent(i * 3 + 1)).setText("");
                        ((JTextArea)panel.getComponent(i * 3 + 2)).setText("");
                    }
                    while (!(read = scanner.nextLine()).isEmpty()) {
                        String value = "", key = "";
                        int j = read.length() - 1;
                        while (read.charAt(j) != ':')
                            j--;
                        value = read.substring(j + 2);
                        j -= 7;
                        int i = j;
                        while (read.charAt(i) != ':')
                            i--;
                        key = read.substring(i + 2, j);
                        ((JTextArea)panel.getComponent(counter * 3 + 1)).setText(key);
                        ((JTextArea)panel.getComponent(counter * 3 + 2)).setText(value);
                        counter++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void initTopRight(Scanner scanner, File base) {
            JPanel panel = view.getStatus();
            int responseCode = scanner.nextInt();
            scanner.nextLine();
            String responseMessage = scanner.nextLine();
            int milis = scanner.nextInt();
            scanner.nextLine();
            long sz = base.length();
            Color temp;
            if (responseCode / 100 == 2)
                temp = new Color(83, 255, 119);
            else if (responseCode / 100 == 3)
                temp = new Color(255, 217, 38);
            else
                temp = new Color(255, 1, 13);
            ((JLabel) panel.getComponent(0)).setText("" + responseCode + " " + responseMessage);
            panel.getComponent(0).setBackground(temp);
            ((JLabel) panel.getComponent(1)).setText("" + milis + " ms");
            ((JLabel) panel.getComponent(2)).setText("" + sz + " bytes");

        }

    }
}
