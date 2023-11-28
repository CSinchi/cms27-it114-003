package Project.client.views;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class LetterGridPanel extends JPanel {
    private JLabel[] labels;

    public LetterGridPanel() {
        setLayout(new GridLayout(3, 9));
        labels = new JLabel[26];
        Border b = BorderFactory.createLineBorder(Color.BLACK, 1);
        for (int i = 0; i < 26; i++) {
            labels[i] = new JLabel(String.valueOf((char) ('A' + i)), SwingConstants.CENTER);
            labels[i].setOpaque(true);
            labels[i].setBorder(b);
            add(labels[i]);
        }
        revalidate();
        repaint();
    }

    public void setLetterStatus(char letter, boolean isLetterCorrect) {
        int index = Character.toUpperCase(letter) - 'A';
        if (index >= 0 && index < 26) {
            labels[index].setBackground(isLetterCorrect? Color.GREEN : Color.RED);
            revalidate();
            repaint();
        }
    }

    public void resetColors() {
        for (JLabel label : labels) {
            label.setBackground(null);
        }
    }
}