package Project.client.views;
import javax.swing.*;

import Project.common.TextFX;

import java.awt.*;

public class HangmanPanel extends JPanel {
    private JLabel label;
    private ImageIcon stage0;
    private ImageIcon stage1;

    public HangmanPanel() {
        setLayout(new BorderLayout());

        
        stage0 = new ImageIcon("Hangman_Stages/Stage_0.png");
        stage1 = new ImageIcon("Hangman_Stages/Stage_1.png");

        // Create a label when first initalize
        label = new JLabel(stage0);

        // Add the label to the panel
        add(label, BorderLayout.CENTER);
    }

    public void changeImage(String strike) {
        switch (strike) {
            case "0":
                label.setIcon(stage0);
                break;
            case "1":
                label.setIcon(stage1);
                break;
            default:
                //insert debugging here
                break;
        }

        // Redraw the panel with the new image
        revalidate();
        repaint();
    }
}