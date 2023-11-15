package Project.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Logger;

import org.w3c.dom.Text;

import Project.common.Constants;
import Project.common.Phase;
import Project.common.TextFX;
import Project.common.TimedEvent;
import Project.common.TextFX.Color;

public class GameRoom extends Room { //Added parts from Ready Check     Cristian Sinchi cms27


    Phase currentPhase = Phase.READY;
    private static Logger logger = Logger.getLogger(GameRoom.class.getName());
    private TimedEvent readyTimer = null; //Is used outside of readys
    private ConcurrentHashMap<Long, ServerPlayer> players = new ConcurrentHashMap<Long, ServerPlayer>();
    private HangmanGame game;

    private ServerPlayer currentTurnPlayer = null;
    private List<ServerPlayer> turnOrder = new ArrayList<ServerPlayer>();

    public GameRoom(String name) {
        super(name);
        
    }

    @Override
    protected void addClient(ServerThread client) {
        logger.info(TextFX.colorize("Adding client as player",Color.BLUE));
        players.computeIfAbsent(client.getClientId(), id -> {
            ServerPlayer player = new ServerPlayer(client);
            super.addClient(client);
            logger.info(String.format(TextFX.colorize("Total clients %s",Color.BLUE), clients.size()));// change visibility to protected
            return player;
        });
    }

    protected void setReady(ServerThread client) {
        logger.info(TextFX.colorize("Ready check triggered", Color.PURPLE));
        if (currentPhase != Phase.READY) {
            logger.warning(String.format("readyCheck() incorrect phase: %s", Phase.READY.name()));
            return;
        }
        if (readyTimer == null) {
            logger.info(String.format(TextFX.colorize("%s Started Timer", Color.PURPLE),client.getClientName()));
            sendMessage(null, "Ready Check Initiated, 30 seconds to join");
            readyTimer = new TimedEvent(30, () -> {
                readyTimer = null;
                readyCheck(true);
            });
        }

        players.values().stream().filter(p -> p.getClient().getClientId() == client.getClientId()).findFirst()
                .ifPresent(p -> {
                    p.setReady(true);
                    logger.info(String.format("Marked player %s[%s] as ready", p.getClient().getClientName(), p
                            .getClient().getClientId()));
                    syncReadyStatus(p.getClient().getClientId());
                });
        readyCheck(false);
    }

    private void readyCheck(boolean timerExpired) {
        if (currentPhase != Phase.READY) {
            return;
        }
        // two examples for the same result
        // int numReady = players.values().stream().mapToInt((p) -> p.isReady() ? 1 :
        // 0).sum();
        long numReady = players.values().stream().filter(ServerPlayer::isReady).count();
        if (numReady >= Constants.MINIMUM_PLAYERS) {

            if (timerExpired) {
                sendMessage(null, "Ready Timer expired, starting session");
                start();
            } else if (numReady >= players.size()) {
                sendMessage(null, "Everyone in the room marked themselves ready, starting session");
                /*if (readyTimer != null) {
                    readyTimer.cancel();
                    readyTimer = null;
                }*/
                cancelReadyTimer();
                start();
            }

        } else {
            if (timerExpired) {
                resetSession();
                sendMessage(null, "Ready Timer expired, not enough players. Resetting ready check");
            }
        }
    }

    private void start() {
        updatePhase(Phase.IN_PROGRESS);
        // TODO example
        /**sendMessage(null, "Session started");
        new TimedEvent(5, () -> resetSession())
                .setTickCallback((time) -> {
                    sendMessage(null, String.format("Example running session, time remaining: %s", time));
                }); */
        Boolean isGameCompleted = false;
        turnOrder = players.values().stream().filter(ServerPlayer::isReady).toList(); //initalize turnOrderList
        game = new HangmanGame();
        logger.info(TextFX.colorize("Game Initializing", Color.PURPLE));
        sendMessage(null, "Started Hangman Game");
        nextTurn();
        
        }

    private synchronized void resetSession() {
        players.values().stream().forEach(p -> p.setReady(false));
        updatePhase(Phase.READY);
        sendMessage(null, "Session ended, please intiate ready check to begin a new one");
    }

    private void updatePhase(Phase phase) {
        if (currentPhase == phase) {
            return;
        }
        currentPhase = phase;
        // NOTE: since the collection can yield a removal during iteration, an iterator
        // is better than relying on forEach
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendPhaseSync(currentPhase);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }

    protected void handleDisconnect(ServerPlayer player) { //temp use this for guess vaildation
        if (players.containsKey(player.getClient().getClientId())) {
            players.remove(player.getClient().getClientId());
            super.handleDisconnect(null, player.getClient()); 
            logger.info(String.format("Total clients %s", clients.size()));
            sendMessage(null, player.getClient().getClientName() + " disconnected");
            if (players.isEmpty()) {
                close();
            }
        }
    }

    private void syncReadyStatus(long clientId) {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendReadyStatus(clientId);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }

    //Serverplayer methods

    private ServerPlayer findPlayer (ServerThread client){
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext())
        {
            ServerPlayer player = iter.next();
            if(player.getClient().getClientId() == client.getClientId()){
                return player;
            }
        }
        return null;
    }

    private void scorePlayer(ServerPlayer player, int score) {

    }

    //guessing handling methods

    protected void handleguessLetter(String guess, ServerThread client) {
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "It is not your turn yet");
            return;
        }
        char letter = guess.charAt(0);
        //if() char is not a letter
        sendMessage(null, client.getClientName() + "  the letter " + guess); 
        if (game.isLetterCorrect(letter)){
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the letter right!");
            scorePlayer(currentTurnPlayer, game.guessedLettersScore(letter));
            checkIsGameWon(); //checks if the max score has been achieved
            sendMessage(null, "New Blanks: " + game.getBlankStr());
            nextTurn();
        }
        else{
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the letter wrong!");
            nextTurn();
        }


    }

    protected void handleSkip(ServerThread client) {
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot skip a turn that is not yours");
        }
        cancelReadyTimer();
        sendMessage(null,client.getClientName()+" skipped thier turned!");
        nextTurn();
    }

    //turn methods from Duengon Project    Cristain Sinchi cms27

    private void nextTurn() { //used to go to next player
        updatePhase(Phase.TURN);
        if (currentTurnPlayer == null) {
            currentTurnPlayer = turnOrder.get(0);
        } else {
            int currentIndex = turnOrder.indexOf(currentTurnPlayer);
            currentIndex++;
            if (currentIndex >= turnOrder.size()) {
                currentIndex = 0;
            }
            currentTurnPlayer= turnOrder.get(currentIndex);
        }
        if (currentTurnPlayer != null) {
            ServerPlayer sp = currentTurnPlayer;
            syncCurrentTurn(sp.getClient().getClientId());
            sendMessage(null, String.format("It's %s's turn to guess", sp.getClient().getClientName()));
            cancelReadyTimer(); //cancel any ongoing timer (in this case Readytimer)
            readyTimer = new TimedEvent(30, () -> {
                sendMessage(null,
                        String.format("%s took to long and has been skipped", sp.getClient().getClientName()));
                nextTurn(); 
            });
        }
    }

    private synchronized void syncCurrentTurn(long clientId) {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendCurrentTurn(clientId);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }

    private void cancelReadyTimer() {
        if (readyTimer != null) {
            readyTimer.cancel();
            readyTimer = null;
        }
    }

    //void check methods for complete win, win round, or lose round

    private void checkIsGameWon() {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer player = iter.next();
            if(player.getScore() >= Constants.HANGMAN_MAX_SCORE) {
                cancelReadyTimer();
                ServerPlayer winningPlayer = getHighScorePlayer(turnOrder);
                sendMessage(null,winningPlayer.getClient().getClientName() + " won the game with the max score of" +winningPlayer.getScore());
                resetSession();
            }
        }     
    }

    //getters

    private ServerPlayer getHighScorePlayer(List<ServerPlayer> players) {
        Iterator<ServerPlayer> iter = this.players.values().stream().iterator();
        ServerPlayer highestScorePlayer = null;
    
        while (iter.hasNext()) {
            ServerPlayer player = iter.next();
            if (highestScorePlayer == null || player.getScore() > highestScorePlayer.getScore()) {
                highestScorePlayer = player;
            }
        }
    
        return highestScorePlayer;
    }

}