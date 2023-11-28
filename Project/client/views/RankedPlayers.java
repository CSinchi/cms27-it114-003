package Project.client.views;

import javax.swing.*;
import java.awt.*;

public class RankedPlayers extends JPanel {
    private JLabel[] ranks;

    public RankedPlayers() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add a border
    }

    public void displayRankings(String[] players) {
        // Remove old rankings
        if (ranks != null) {
            for (JLabel rank : ranks) {
                remove(rank);
            }
        }

        // Create new rankings
        ranks = new JLabel[players.length];
        for (int i = 0; i < players.length; i++) {
            ranks[i] = new JLabel(players[i]);
            add(ranks[i]);
        }

        revalidate();
        repaint();
    }
}

