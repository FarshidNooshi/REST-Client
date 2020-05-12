import javax.swing.*;
import java.awt.*;

public class Options extends JFrame {
    private boolean isDarkMode, isExitable;
    private boolean followRedirect;
    private JCheckBox followRedirectBox, darkModeBox, exitBox;
    private View view;

    Options() {
        setTitle("Options");
        setPreferredSize(new Dimension(400, 200));
        setIconImage(new ImageIcon("OrbitProject/res/Insomnia.png").getImage());
        setLayout(new GridLayout(3, 1));
        isDarkMode = false;
        isExitable = false;
        followRedirect = false;
        followRedirectBox = new JCheckBox("Follow Redirect");
        darkModeBox = new JCheckBox("Dark Mode");
        exitBox = new JCheckBox("Exit ?");
        add(followRedirectBox);
        add(darkModeBox);
        add(exitBox);

        darkModeBox.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            if (isDarkMode)
                view.setToDark();
            else
                view.setToLight();
        });

        followRedirectBox.addActionListener(e -> {
            followRedirect = !followRedirect;
            System.out.println(followRedirect ? "follow" : "don't follow");
        });

        exitBox.addActionListener(e -> {
            isExitable = !isExitable;
            System.out.println(isExitable ? "exit" : "get out");
        });
    }

    public void showGUI() {
        pack();
        setVisible(true);
    }

    public void setView(View view) {
        this.view = view;
    }
}
