package Phase3;

import Phase1.Requests;
import Phase1.View;
import Phase2.Reader;
import Phase2.Request;
import Phase4.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Controller {
    private ActionListener actionListener;
    private View view;
    private Request request;
    private StringBuilder headersBuilder = new StringBuilder();

    /**
     * constructor is this
     *
     * @param view is the program gui
     */
    Controller(View view) {
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

    /**
     * initializing the settings and getting the preferred listeners to the components.
     */
    void establish() {
        initLeftListeners();
        addTrashListener(View.getCenterPanels().get(0));
        addTrashListener(View.getCenterPanels().get(2));
        addTrashListener(View.getFormDataPanel());
        initBinaryBody();
        initBodyListeners();
        view.getSendURL().addActionListener(actionListener);
        view.getCopy().addActionListener(e -> {
            String myString = headersBuilder.toString();
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
    }

    /**
     * this method inits the listener for bodyTabbedPane
     */
    private void initBodyListeners() {
        JTabbedPane bodyTabbedPane = view.getBodyTabbedPane();
        bodyTabbedPane.addChangeListener(e -> {
            if (bodyTabbedPane.getSelectedComponent().getName().equalsIgnoreCase("formData")) {
                String str = JOptionPane.showInputDialog(view, "Type encoded if you want to send urlEncoded or formData otherwise", JOptionPane.INPUT_VALUE_PROPERTY);
                request.getMp().replace("type", str);
            }
        });
    }

    /**
     * initializing buttons for uploading binary
     */
    private void initBinaryBody() {
        JButton choose = view.getSelectItem();
        choose.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser("OrbitProject\\src");
            fileChooser.setPreferredSize(new Dimension(750, 750));
            fileChooser.showOpenDialog(view);
            fileChooser.setMultiSelectionEnabled(false);
            JLabel label = view.getFileSelected();
            label.setText("File selected: " + fileChooser.getSelectedFile().getName());
            label.setIcon(new ImageIcon("OrbitProject/Data/save_close_100px.png"));
            request.getMp().replace("uploadBinary", fileChooser.getSelectedFile().getAbsolutePath());
            request.getMp().replace("type", "binary");
        });
        JButton reset = view.getResetItem();
        reset.addActionListener(e -> {
            JLabel label = view.getFileSelected();
            label.setText("File selected: " + "Nothing");
            label.setIcon(new ImageIcon("OrbitProject/Data/file_100px.png"));
            request.getMp().replace("uploadBinary", "");
            request.getMp().replace("type", "");
        });
    }

    /**
     * initializing trash buttons listeners
     *
     * @param panel is the panel to be initialized
     */
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

    /**
     * initializing the left panel
     */
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
                    final JFileChooser fileChooser = new JFileChooser("OrbitProject\\src" + File.separator + folders.get(index));
                    fileChooser.setPreferredSize(new Dimension(1000, 1000));
                    fileChooser.showOpenDialog(view);
                    fileChooser.setMultiSelectionEnabled(false);
                    try {
                        Reader reader = new Reader(fileChooser.getSelectedFile().getAbsolutePath());
                        request = (Request) reader.ReadFromFile();
                        reader.close();
                        view.getSendURL().setEnabled(false);
                        view.getUrlTextField().setEnabled(false);
                        Worker worker = new Worker();
                        worker.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * executes the services.
     *
     * @param request is the request to be handled.
     * @throws Exception if occurs
     */
    private Response executeService(Request request) throws Exception {
        if (!request.getMp().get("save").equals("false"))
            request.SaveRequest();
        if (request.getMp().get("proxy").equals("true")) {
            try (Socket socket = new Socket(request.getMp().get("ip"), Integer.parseInt(request.getMp().get("port")))) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(request);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                try {
                    return (Response) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Phase2.HTTpService service = new Phase2.HTTpService(request);
        return service.runService();
    }

    /**
     * swing worker is this.
     */
    private class Worker extends SwingWorker<Response, Object> {

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
        protected Response doInBackground() throws Exception {
            view.getRaw().setText("");
            view.getJson().setText("");
            view.getBufferedPic().setIcon(null);
            return initRequest();
        }

        /**
         * initializing the Request. and building it with the user inputs to the gui.
         *
         * @throws Exception if occurs
         */
        private Response initRequest() throws Exception {
            if (request.getMp().get("i").equals("true")) {
                request.getMp().replace("save", "false");
                return executeService(request);
            }
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
            if (view.getList().getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(view, "choose or create a folder to save your request from the left panel", "Save Error", JOptionPane.ERROR_MESSAGE);
                done();
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
                if (request.getMp().get("type").equals("")) {
                    String str = JOptionPane.showInputDialog(view, "Type encoded if you want to send urlEncoded or formData otherwise", JOptionPane.INPUT_VALUE_PROPERTY);
                    request.getMp().replace("type", str);
                }
            } else if (bodyTabbedPane.getSelectedComponent().getName().contains("binary")) {
                request.getMp().replace("type", "binary");
            }
            if (view.getOptions().getProxy().isSelected()) {
                request.getMp().replace("proxy", "true");
                request.getMp().replace("ip", view.getOptions().getIp().getText());
                request.getMp().replace("port", view.getOptions().getPort().getText());
            }
            return executeService(request);
        }

        /**
         * like the done of the SwingWorker this is a method to do the final job after finishing the doInBackground
         */
        public void done() {
            view.getSendURL().setEnabled(true);
            view.getUrlTextField().setEnabled(true);
            view.getUrlTextField().setText(request.getMp().get("url"));
            view.getRaw().setText("");
            switch (request.getMp().get("method")) {
                case "PUT" :
                    view.getComboBox().setSelectedIndex(0);
                    break;
                case "POST":
                    view.getComboBox().setSelectedIndex(1);
                    break;
                case "DELETE":
                    view.getComboBox().setSelectedIndex(2);
                    break;
                case "GET":
                    view.getComboBox().setSelectedIndex(3);
            }
            try {
                Response response = get();
                initTopRight(response);
                view.getRaw().setText(response.getBody());
                JPanel panel = View.getRightPanels().get(1);
                int counter = 0;
                for (int i = 0; i < 15; i++) {
                    ((JTextArea) panel.getComponent(i * 3 + 1)).setText("");
                    ((JTextArea) panel.getComponent(i * 3 + 2)).setText("");
                }
                headersBuilder = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
                    ((JTextArea) panel.getComponent(counter * 3 + 1)).setText(entry.getKey());
                    ((JTextArea) panel.getComponent(counter * 3 + 2)).setText(entry.getValue().toString());
                    counter++;
                    headersBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
                }
                initRight(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            request = new Request();
        }

        /**
         * initializing the right panels such as headers and bodies. this method doesn't initializes the topRight frame.
         */
        private void initRight(Response response) {
            if (response.getHeaders().get("Content-Type").get(0).contains("json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //noinspection deprecation
                JsonParser jp = new JsonParser();
                //noinspection deprecation
                JsonElement je = jp.parse(view.getRaw().getText());
                String prettyJsonString = gson.toJson(je);
                view.getJson().setText(prettyJsonString);
            }
            try {
                view.getPreview().setPage(view.getUrlTextField().getText());
                if (response.getHeaders().get("Content-Type").get(0).contains("image")) {
                    URL url = new URL(view.getUrlTextField().getText());
                    Image image = ImageIO.read(url);
                    view.getBufferedPic().setIcon(new ImageIcon(image));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * initializing the topRight panel and adding the colors with theme.
         */
        private void initTopRight(Response response) {
            JPanel panel = view.getStatus();
            int responseCode = response.getResponseCode();
            String responseMessage = response.getResponseMessage();
            int millis = response.getMillis();
            long sz = response.getSz();
            Color temp;
            if (responseCode / 100 == 2)
                temp = new Color(83, 255, 119);
            else if (responseCode / 100 == 3)
                temp = new Color(255, 217, 38);
            else if (responseCode / 100 == 4)
                temp = new Color(255, 1, 13);
            else
                temp = new Color(54, 201, 255);
            ((JLabel) panel.getComponent(0)).setText("" + responseCode + " " + responseMessage);
            panel.getComponent(0).setBackground(temp);
            ((JLabel) panel.getComponent(1)).setText("" + millis + " ms");
            ((JLabel) panel.getComponent(2)).setText("" + sz + " bytes");
        }

    }
}