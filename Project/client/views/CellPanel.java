package Project.client.views;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Project.client.Client;


public class CellPanel extends JPanel {
    private static final List<CellPanel> allCells = new ArrayList<>();
    private boolean selected = false;
    private int x, y;
    private JTextField jtf;

    public CellPanel() {
        allCells.add(this); // Add this instance to the list of all cells
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;
                // setBackground(selected ? Color.GREEN : Color.WHITE);
                updateAllBorders(); // Update the borders of all cells
                setBorder(BorderFactory.createLineBorder(selected ? Color.CYAN : Color.BLACK));
                promptAction();
            }
        });
        jtf = new JTextField("");
        jtf.setEditable(false);
        this.add(jtf);
    }

    private void promptAction() {
        // Show JOptionPane with options
        Object[] options = { "Move", "Attack", "Heal" };
        int result = JOptionPane.showOptionDialog(null, "Choose an action", "Action",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        // Handle the result
        try {
            switch (result) {
                case 0: // Move
                    // Handle move action
                    break;
                case 1: // Attack
                    // Handle attack action
                    break;
                case 2: // Heal
                    // Handle heal action
                    break;
                default:
                    // Handle case where user closed the dialog without choosing an option
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update the borders of all cells
    private void updateAllBorders() {
        for (CellPanel cell : allCells) {
            if (cell != this) { // Don't update the border of the cell that was clicked
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.selected = false;
            }
        }
    }

    public void setOccupiedCount(int c) {
        jtf.setText(c + "");
    }


    public static void reset() {
        allCells.clear();
    }
}