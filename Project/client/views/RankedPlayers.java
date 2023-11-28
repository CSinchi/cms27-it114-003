package Project.client.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RankedPlayers extends JPanel {
    private List<String> rankings;

    public RankedPlayers() {
        super(new BorderLayout());
        this.rankings = new ArrayList<>();
        updateRanking();
    }

    public void updateRanking(String[] newStrings) {
        this.rankings.clear();
        for (String s : newStrings) {
            this.rankings.add(s);
        }
        updateRanking();
    }

    private void updateRanking() {
        removeAll();

        JPanel rankPanel = new JPanel();
        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.Y_AXIS));

        Iterator<String> iter = rankings.iterator();
        int index = 0;
        while (iter.hasNext()) {
            String string = rankings.get(index);
            JLabel stringLabel = new JLabel(string);
            rankPanel.add(stringLabel);
        }

        JScrollPane scrollPane = new JScrollPane(rankPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); //Border

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}

