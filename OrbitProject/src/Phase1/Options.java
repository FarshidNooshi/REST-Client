package Phase1;// In The Name Of GOD

import javax.swing.*;
import java.awt.*;

public class Options extends JFrame {
    private JCheckBox followRedirectBox, darkModeBox, exitBox, proxy;
    private JTextField ip, port;
    private View view;
    private JPanel panel;

    /**
     * it's the constructor of the options frame
     */
    Options() {
        setTitle("Options");
        setPreferredSize(new Dimension(400, 200));
        setIconImage(new ImageIcon("OrbitProject/Data/Insomnia.png").getImage());
        setLayout(new GridLayout(4, 1, 10, 20));
        panel = new JPanel(new GridLayout(1, 3, 10, 10));
        followRedirectBox = new JCheckBox("Follow Redirect");
        darkModeBox = new JCheckBox("Dark Mode");
        exitBox = new JCheckBox("Exit and Go to system tray");
        proxy = new JCheckBox("Proxy");
        ip = new JTextField();
        port = new JTextField();
        add(followRedirectBox);
        add(darkModeBox);
        add(exitBox);
        add(panel);
        panel.add(proxy);
        panel.add(ip);
        panel.add(port);
        ip.setEnabled(false);
        port.setEnabled(false);
        ip.setText("ip:");
        port.setText("port:");
        port.setToolTipText("port");
        ip.setToolTipText("ip");

        darkModeBox.addActionListener(e -> {
            if (darkModeBox.isSelected())
                view.setToDark();
            else
                view.setToLight();
        });

        followRedirectBox.addActionListener(e -> {
            System.out.println(followRedirectBox.isSelected() ? "follow" : "don't follow");
        });

        exitBox.addActionListener(e -> {
            System.out.println(exitBox.isSelected() ? "exit" : "get out");
            view.setToTray();
            if (exitBox.isSelected())
                dispose();
        });

        proxy.addActionListener(e -> {
            if (!proxy.isSelected()) {
                ip.setEnabled(false);
                port.setEnabled(false);
                ip.setText("ip:");
                port.setText("port:");
            } else {
                ip.setText("");
                port.setText("");
                ip.setEnabled(true);
                port.setEnabled(true);
            }
        });
    }

    /**
     * showing the gui of this frame to the user
     */
    public void showGUI() {
        pack();
        setVisible(true);
    }

    public JCheckBox getDarkModeBox() {
        return darkModeBox;
    }

    public JCheckBox getExitBox() {
        return exitBox;
    }

    public JCheckBox getFollowRedirectBox() {
        return followRedirectBox;
    }

    public JTextField getIp() {
        return ip;
    }

    public JTextField getPort() {
        return port;
    }

    public JCheckBox getProxy() {
        return proxy;
    }

    /**
     * setting the view for the users
     *
     * @param view is the program view class
     */
    public void setView(View view) {
        this.view = view;
    }
}