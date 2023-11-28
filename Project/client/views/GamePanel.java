package Project.client.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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

import Project.client.Card;
import Project.client.Client;
import Project.client.ICardControls;
import Project.client.IGameEvents;
import Project.common.Phase;

public class GamePanel extends JPanel implements IGameEvents {
    private JPanel hgamePanel;
    private CardLayout cardLayout;

    private JLabel timer;
    private JLabel turnStatus;
    private JLabel blankWord;
    private LetterGridPanel letterGrid;
    private RankedPlayers rankedPlayers;

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
        //createOptionsPanel();  TODO change from characters to turn options
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

    /*private void createOptionsPanel() {
        JPanel characterOptions = new JPanel();
        JButton tank = new JButton();
        tank.setText("Tank");
        tank.addActionListener(l -> {
            try {
                Client.INSTANCE.sendCreateCharacter(CharacterType.TANK);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        JButton damage = new JButton();
        damage.setText("Attacker");
        damage.addActionListener(l -> {
            try {
                Client.INSTANCE.sendCreateCharacter(CharacterType.DAMAGE);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        JButton support = new JButton();
        support.setText("Support");
        support.addActionListener(l -> {
            try {
                Client.INSTANCE.sendCreateCharacter(CharacterType.SUPPORT);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        characterOptions.add(tank);
        characterOptions.add(damage);
        characterOptions.add(support);
        add(characterOptions);
    } */

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
        /*cells = new CellPanel[rows][columns];
        gridPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new CellPanel();
                cells[i][j].setType(CellType.NONE, i, j, false);
                gridPanel.add(cells[i][j]);
            }
        }*/
        hgamePanel.revalidate();
        hgamePanel.repaint(); 
    } 

    private void createTopHGPanel() {
        JPanel topPanel =new JPanel(new BorderLayout());
        turnStatus = new JLabel("Current Turn:", SwingConstants.LEFT); //Turn Status
        topPanel.add(turnStatus, BorderLayout.WEST);
        timer = new JLabel("00"); //Turn Timer
        topPanel.add(timer, BorderLayout.CENTER);
        hgamePanel.add(topPanel, BorderLayout.NORTH);
    }

    private void createBottomHGPanel(){
        JPanel bottomPanel = new JPanel( new BorderLayout());
        JButton guessWordButton = new JButton("Guess a Word");
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
        JButton guessLetterButton = new JButton("Guess a Letter");
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
        //TODO add letter guess
        JButton skipButton = new JButton("Skip Turn");
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

    private void createCenterHGPanel(){
        JPanel baseCenterPanel = new JPanel(new BorderLayout());//holder for all objects in this center panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        blankWord = new JLabel("Blank", SwingConstants.CENTER);
        hgamePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int size = hgamePanel.getWidth() / 20; // Adjust this ratio as needed
                blankWord.setFont(new Font(blankWord.getFont().getName(),Font.PLAIN, size));
            }
        });
        mainPanel.add(blankWord, BorderLayout.WEST);
        baseCenterPanel.add(mainPanel, BorderLayout.CENTER);
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
        hgamePanel.add(rankedPlayers, BorderLayout.EAST);
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
        turnStatus.setText("Current Turn: " + player);
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

    }

    public void onReceiveRankedPlayers(String[] players) {
        rankedPlayers.updateRanking(players);
        hgamePanel.revalidate();
        hgamePanel.repaint();
    }
    /*@Override
    public void onReceiveCell(List<Cell> cells) {
        for (Cell c : cells) {
            CellPanel target = this.cells[c.getX()][c.getY()];
            target.setType(c.getCellType(), c.getX(), c.getY(), c.isBlocked());
            target.setOccupiedCount(c.getCharactersInCell().size());
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    @Override
    public void onReceiveGrid(int rows, int columns) {
        resetView();
        if (rows > 0 && columns > 0) {
            makeGrid(rows, columns);
        }
    }

    @Override
     public void onReceiveCharacter(long clientId, Character character) {
        // kind of a sideways way of interacting with the ChatPanel, will likely
        // refactor this later
        ChatGamePanel cgp = (ChatGamePanel) this.getParent().getParent();
        cgp.getChatPanel().addText(Client.INSTANCE.getClientNameById(clientId) + " summoned " + character.getName());
    } */
}
