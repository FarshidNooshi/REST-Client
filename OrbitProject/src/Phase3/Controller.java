package Phase3;

import Phase1.View;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    private View view;

    public Controller(View view) {
        this.view = view;
        view.getSaveURL().addActionListener(e -> {
            view.getSaveURL().setEnabled(false);
            view.getUrlTextField().setEnabled(false);
            view.getTabbedPane().getSelectedComponent().setVisible(false);
        });
    }

    public void establish() {
        JButton createNode = view.getCreateNode();
        JList<Object> list = view.getList();
        ArrayList<String> folders = view.getFolders();
        createNode.addActionListener(e -> {
            String str = JOptionPane.showInputDialog(null, "Type a name", JOptionPane.INPUT_VALUE_PROPERTY);
            File fileToCreate = new File("OrbitProject\\src" + File.separator + str);
            if (fileToCreate.mkdir()) {
                folders.add(folders.size(), str);
                System.out.println("directory created!");
            }
            else
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
                }
            }
        });
    }
}

class Worker extends SwingWorker<Integer, Object> {

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
    protected Integer doInBackground() throws Exception {
        return null;
    }
}
