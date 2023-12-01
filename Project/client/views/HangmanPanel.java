package Project.client.views;
import javax.swing.*;


import java.awt.*;
import java.awt.event.ComponentListener;

public class HangmanPanel extends JPanel {
    private JLabel label;
    private ImageIcon currentStage;
    private ImageIcon stage0;
    private ImageIcon stage1;
    private ImageIcon stage2;
    private ImageIcon stage3;
    private ImageIcon stage4;
    private ImageIcon stage5;
    private ImageIcon stage6;

    //private int defualtWidth = 100;
    //private int defaultHeight = 125;  1:1.25 ratio for our image

    private int currentWidth = 30;

    public HangmanPanel() {
        setLayout(new BorderLayout());
        
        stage0 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_0.png"));
        stage1 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_1.png"));
        stage2 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_2.png"));
        stage3 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_3.png"));
        stage4 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_4.png"));
        stage5 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_5.png"));
        stage6 = new ImageIcon(getClass().getResource("Hangman_Stages/Stage_6.png")); 

        // Initalize stage 0 when first called
        label = new JLabel();
        currentStage = stage0;
        scaleImage(currentWidth);
    
        // Add image label to the panel
        add(label, BorderLayout.CENTER);

    }

    public void changeImage(String strike) {
        switch (strike) {
            case "0":
                currentStage = stage0;
                scaleImage(currentWidth);
                break;
            case "1":
                currentStage = stage1;
                scaleImage(currentWidth);
                break;
            case "2":
                currentStage = stage2;
                scaleImage(currentWidth);
                break;
            case "3":
                currentStage = stage3;
                scaleImage(currentWidth);
                break;
            case "4":
                currentStage = stage4;
                scaleImage(currentWidth);
                break;
            case "5":
                currentStage = stage5;
                scaleImage(currentWidth);
                break;
            case "6":
                currentStage = stage6;
                scaleImage(currentWidth);
                break;
            default:
                //insert debugging here
                break;
        }

        // Redraw the panel with the new image
        revalidate();
        repaint();
    }
    
    public void scaleImage(int width) {
        currentWidth = width;
        scaleImage();
    }

    private void scaleImage() {
        Image img = currentStage.getImage();
        int height = (int) (currentWidth + currentWidth * 0.25);
        Image imgScale = img.getScaledInstance(currentWidth, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        label.setIcon(scaledIcon);
        revalidate();
        repaint();
    } 
}