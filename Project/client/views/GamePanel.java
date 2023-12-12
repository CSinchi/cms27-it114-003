package Project.client.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Project.client.Card;
import Project.client.Client;
import Project.client.ICardControls;
import Project.client.IGameEvents;
import Project.common.Phase;

//From Drawing Grid Project Heavely modifed for this project


public class GamePanel extends JPanel implements IGameEvents {
    private JPanel hgamePanel;
    private CardLayout cardLayout;

    private JLabel timer;
    private JLabel turnStatus;
    private JLabel blankWord;
    private JLabel round;
    private JLabel strikes;
    private LetterGridPanel letterGrid;
    private RankedPlayers rankedPlayers;
    private HangmanPanel hangmanImage;

    public GamePanel(ICardControls controls) {
        super(new CardLayout());
        cardLayout = (CardLayout) this.getLayout();
        this.setName(Card.GAME_SCREEN.name());
        Client.INSTANCE.addCallback(this);
        // this is purely for debugging
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("GamePanel Resized to " + e.getComponent().getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // System.out.println("Moved to " + e.getComponent().getLocation());
            }
        });
        createReadyPanel();
        hgamePanel = new JPanel(new BorderLayout());
        createHGamePanel();
        add(hgamePanel);
        setVisible(false);
        // don't need to add this to ClientUI as this isn't a primary panel(it's nested
        // in ChatGamePanel)
        // controls.addPanel(Card.GAME_SCREEN.name(), this);
    }

    private void createReadyPanel() {
        JPanel readyPanel = new JPanel();
        JButton readyButton = new JButton();
        readyButton.setText("Ready");
        readyButton.addActionListener(l -> {
            try {
                Client.INSTANCE.sendReadyStatus();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        readyPanel.add(readyButton);
        this.add(readyPanel);
    }

    private void resetView() {
        if (hgamePanel == null) {
            return;
        }
        if (hgamePanel.getLayout() != null) {
            hgamePanel.setLayout(null);
        }
        hgamePanel.removeAll();
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    private void createHGamePanel() {
        createTopHGPanel();
        createBottomHGPanel();
        //createLeftHGPanel();
        createRightHGPanel();
        createCenterHGPanel();
        
        hgamePanel.revalidate();
        hgamePanel.repaint(); 
    } 

    private void createTopHGPanel() { //sets up north part of gamepanel     cms27 11/27/23
        JPanel topPanel =new JPanel(new BorderLayout());

        JPanel turnPanel = new JPanel(new BorderLayout()); //creates turn panel
        turnStatus = new JLabel("Current Turn:", SwingConstants.LEFT); //Create Turn Status 
        timer = new JLabel("00"); //Create Turn Timer
        turnPanel.add(turnStatus, BorderLayout.WEST);//add turn status to turn panel west
        turnPanel.add(timer, BorderLayout.CENTER); //add turn timer to turn panel center
        topPanel.add(turnPanel, BorderLayout.EAST); //add turn panel to top panel east

        round = new JLabel("Round:", SwingConstants.LEFT); //Create Round Status
        topPanel.add(round, BorderLayout.WEST);//add round status to top panel center

        strikes = new JLabel("Strikes:" , SwingConstants.CENTER); //Create Strikes Status
        topPanel.add(strikes, BorderLayout.CENTER); //add strike status to top panel east

        hgamePanel.add(topPanel, BorderLayout.NORTH);
    }

    private void createBottomHGPanel(){ //sets up bottom part of game panel     cms27 12/26/23
        JPanel bottomPanel = new JPanel( new BorderLayout());
        JButton guessWordButton = new JButton("Guess a Word"); //button that sends guess word data to server
        guessWordButton.addActionListener(l -> {
            try {
                String word = JOptionPane.showInputDialog(null, "Enter your guess:", "Guess a Word", JOptionPane.PLAIN_MESSAGE);
                    if (word != null) {
                        Client.INSTANCE.sendGuessWord(word);
                    }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        bottomPanel.add(guessWordButton, BorderLayout.WEST);

        JButton guessLetterButton = new JButton("Guess a Letter"); //button that sends guess letter data to server
        guessLetterButton.addActionListener(l -> {
            try {
                String[] options = new String[26];
                for (int i = 0; i < 26; i++) {
                    options[i] = String.valueOf((char) ('A' + i));
                }
                String letter = (String) JOptionPane.showInputDialog(null, "Choose a letter",
                "Letter Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (letter != null) {
                        Client.INSTANCE.sendGuessLetter(letter);
                    }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        bottomPanel.add(guessLetterButton, BorderLayout.CENTER);
  
        JButton skipButton = new JButton("Skip Turn"); //button that sends skip data to server
        skipButton.addActionListener(l -> {
            try {
                Client.INSTANCE.sendSkipStatus();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        bottomPanel.add(skipButton, BorderLayout.EAST);
        hgamePanel.add(bottomPanel,BorderLayout.SOUTH);

    }

    private void createCenterHGPanel(){ //sets up center part of gamepanel      cms27 11/27/23 
        JPanel baseCenterPanel = new JPanel(new BorderLayout());//holder for all objects in this center panel

        JPanel mainPanel = new JPanel(new BorderLayout()); //create core center panel
        blankWord = new JLabel("Blank", SwingConstants.CENTER); //Create Blank Word holder
       
        mainPanel.add(blankWord, BorderLayout.WEST);//add blank word to core center west
        hangmanImage = new HangmanPanel(); //Initalize Hangman images panel 
        mainPanel.add(hangmanImage, BorderLayout.EAST);//add image panel to core center east
        baseCenterPanel.add(mainPanel, BorderLayout.CENTER);//add core center to center panel
        hgamePanel.addComponentListener(new ComponentAdapter() { //listner when the panel is resized 
            @Override
            public void componentResized(ComponentEvent e) {
                int size = hgamePanel.getWidth() / 20; //font size for blank
                blankWord.setFont(new Font(blankWord.getFont().getName(),Font.PLAIN, size)); //sets blank word size
                hangmanImage.scaleImage(hgamePanel.getWidth() / 3); //scales hangman image size
            }
        });

        letterGrid = new LetterGridPanel(); //Initalize letter grid object
        baseCenterPanel.add(letterGrid,BorderLayout.SOUTH); //add letter grid to center panel to the south

        hgamePanel.add(baseCenterPanel, BorderLayout.CENTER);
    }   

    private void createLeftHGPanel(){
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel test = new JLabel("test");
        leftPanel.add(test, BorderLayout.NORTH);
        hgamePanel.add(leftPanel, BorderLayout.WEST);
    }

    private void createRightHGPanel(){
        rankedPlayers = new RankedPlayers();
        JPanel mainrightPanel = new JPanel(new BorderLayout());
        JPanel centerrightPanel = new JPanel(new BorderLayout());
        JButton awayButton = new JButton("Mark Away"); //button that sends skip data to server
        awayButton.addActionListener(l -> {
            try {
                Client.INSTANCE.sendAwayStatus();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainrightPanel.add(awayButton, BorderLayout.SOUTH);
        centerrightPanel.add(rankedPlayers, BorderLayout.NORTH);
        mainrightPanel.add(centerrightPanel,BorderLayout.CENTER);
        hgamePanel.add(mainrightPanel, BorderLayout.EAST);
        //hgamePanel.add(rankedPlayers, BorderLayout.EAST);
    }

    
    @Override
    public void onClientConnect(long id, String clientName, String message) {
    }

    @Override
    public void onClientDisconnect(long id, String clientName, String message) {
    }

    @Override
    public void onMessageReceive(long id, String message) {
    }

    @Override
    public void onReceiveClientId(long id) {
    }

    @Override
    public void onSyncClient(long id, String clientName) {
    }

    @Override
    public void onResetUserList() {
    }

    @Override
    public void onReceiveRoomList(String[] rooms, String message) {
    }

    @Override
    public void onRoomJoin(String roomName) {
    }

    @Override
    public void onReceivePhase(Phase phase) {
        // I'll temporarily do next(), but there may be scenarios where the screen can
        // be inaccurate
        System.out.println("Received phase: " + phase.name());
        if (phase == Phase.READY) {
            if (!isVisible()) {
                setVisible(true);
                this.getParent().revalidate();
                this.getParent().repaint();
                System.out.println("GamePanel visible");
            } else {
                cardLayout.next(this);
            }
        } else if (phase == Phase.IN_PROGRESS) {
            cardLayout.next(this);
        }
    }

    @Override
    public void onReceiveReady(long clientId) {
    }

    public void onReceiveTurn(String player){
        turnStatus.setText("Current Turn: " + player + " ");
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    @Override
    public void onReceiveTime(String time) {
        timer.setText(time);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    @Override
    public void onReceiveBlankWord(String word) {
        blankWord.setText(word);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    public void onReceiveLetterStat(String letter, Boolean isCorrect) {
        char l = letter.charAt(0);
        letterGrid.setLetterStatus(l, isCorrect);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    public void onReceiveRound(String round) {
        letterGrid.resetColors();
        this.round.setText("Round: " + round);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }

    public void onReceiveRankedPlayers(String[] players) {
        rankedPlayers.displayRankings(players);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }
    
    public void onReceiveStrike(String strike) {
        strikes.setText("Strikes: " + strike);
        hangmanImage.changeImage(strike);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }
}
