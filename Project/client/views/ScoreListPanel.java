package Project.client.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreListPanel extends JPanel {
    /*private List<> playerScores;

    public RankingPanel() {
        super(new BorderLayout());
        this.playerScores = new ArrayList<>();
        updateRankings();
    }

    public void updateRankings(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
        updateRankings();
    }

    private void updateRankings() {
        removeAll();

        // Sort clients based on score in descending order
        playerScores.sort(Comparator.comparingInt(PlayerScore::getScore).reversed());

        JPanel rankingPanel = new JPanel();
        rankingListPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < playerScores.size(); i++) {
            PlayerScore playerScore = playerScores.get(i);
            JLabel rankingLabel = new JLabel((i + 1) + ". " + playerScore.getName() + " - " + playerScore.getScore());
            rankingPanel.add(rankingLabel);
        }

        JScrollPane scrollPane = new JScrollPane(rankingPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    } */
}

