package Phase1;// In The Name Of GOD

import javax.swing.*;
import java.awt.*;

public class Options extends JFrame {
    private JCheckBox followRedirectBox, darkModeBox, exitBox;
    private View view;

    /**
     * it's the constructor of the options frame
     */
    Options() {
        setTitle("Options");
        setPreferredSize(new Dimension(400, 200));
        setIconImage(new ImageIcon("OrbitProject/Data/Insomnia.png").getImage());
        setLayout(new GridLayout(3, 1));
        followRedirectBox = new JCheckBox("Follow Redirect");
        darkModeBox = new JCheckBox("Dark Mode");
        exitBox = new JCheckBox("Exit&go to system tray ?");
        add(followRedirectBox);
        add(darkModeBox);
        add(exitBox);

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

    /**
     * setting the view for the users
     *
     * @param view is the program view class
     */
    public void setView(View view) {
        this.view = view;
    }
}