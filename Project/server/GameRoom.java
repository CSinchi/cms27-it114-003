package Project.server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Logger;

import org.w3c.dom.Text;

import Project.common.Constants;
import Project.common.Payload;
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
    private boolean isGameRunning;

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
        turnOrder = players.values().stream().filter(ServerPlayer::isReady).toList(); //initalize turnOrderList
        logger.info(TextFX.colorize("Game Initializing", Color.PURPLE));
        game = new HangmanGame();
        sendMessage(null, "Started Hangman Game");
        announceRound();
        nextTurn();
        
        }

    private synchronized void resetSession() {
        players.values().stream().forEach(p -> p.setReady(false));
        players.values().stream().forEach(p -> p.setScore(0)); //addition to resetSession to also clear out players data when called
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

    protected void handleDisconnect(ServerPlayer player) { 
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

    protected void announceRound() {
        logger.info(String.format(TextFX.colorize("This Round [%d] word is %s",Color.PURPLE),game.getCurrentRound(),game.getCurrentWord()));
        sendMessage(null, "Round " + game.getCurrentRound() + " Blank Word: " + game.getBlankStr());
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
        player.addScore(score);
        logger.info(String.format(TextFX.colorize("%s scored %d points", Color.PURPLE),player.getClient().getClientName(), score));
        sendMessage(null, String.format("%s scored %d points!",player.getClient().getClientName(), score));
        //sendMessage(null, String.format("&s's total score is %d points", player.getClient().getClientName(), player.getScore()));
      }

    private void displayPlayersScoreRanked(List<ServerPlayer> players){
        List<ServerPlayer> rankedList = new ArrayList<>(players);
        rankedList.sort(Comparator.comparing(ServerPlayer::getScore).reversed());
        StringBuilder sb = new StringBuilder(100);
        Iterator<ServerPlayer> iter = rankedList.iterator();
        int rankNum = 1;
        while(iter.hasNext()){
            sb.append(" ");
            ServerPlayer player = iter.next();
            String s = String.format("[%d] %s-%d", rankNum, player.getClient().getClientName(),player.getScore());
            sb.append(s);

        }
        sendMessage(null, "Score Rankings:"+ sb);

    }

    //guessing handling methods

    protected void handleGuessLetter(String guess, ServerThread client) {
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "It is not your turn yet");
            return;
        }
        char letter = guess.charAt(0);

        sendMessage(null, client.getClientName() + " guessed the letter " + guess); 
        if (game.isLetterCorrect(letter)){
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the letter right!");
            scorePlayer(currentTurnPlayer, game.guessedLettersScore(letter));
                if (!checkIsPlayerWon()) {//checks if the max score win condition has been achieved
                sendMessage(null, "New Blank Word: " + game.getBlankStr());  //Sends out a new blank to clients
                checkIsRoundCompleted(); //goes to next round if applicable (blank word is completed)
                    if(!checkIsGameCompleted()){
                        nextTurn();
                    } 
                }
            }
        else{
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the letter wrong!");
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            checkIsRoundCompleted(); //goes to next round if applicable (hangman completed)
            if(!checkIsGameCompleted()){
                nextTurn();
            }
        }
    }

    protected void handleGuessWord(String guess, ServerThread client) {
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "It is not your turn yet");
            return;
        }
        sendMessage(null, client.getClientName() + " guessd the word " + guess);
        if (game.isWordCorrect(guess)){
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the word right!");
            scorePlayer(currentTurnPlayer, game.guessedWordScore(guess));
                if(!checkIsPlayerWon()){
                    sendMessage(null, "New Blank Word: " + game.getBlankStr());  //Sends out a new blank to clients
                    checkIsRoundCompleted();
                    if(!checkIsGameCompleted()){//will go to next turn until win condition is set true
                        nextTurn();
                    }
                }  
        }   

        else {
            cancelReadyTimer();
            sendMessage(null, client.getClientName() + " got the word wrong!");
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            checkIsRoundCompleted(); //goes to next round if applicable (hangman completed)
            if(!checkIsGameCompleted()){ //will go to next turn until win condition is set true
                nextTurn();
            }
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

     public boolean hasLetters (String str, ServerThread client) {
        if (str == ""){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot send blanks");
            return false;
        }
        char[] explodedString = str.toCharArray();
        for (int i = 0; i < explodedString.length; i++){
            if(!Character.isLetter(explodedString[i])){
                client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot send numbers or speical characters");
                return false;
            }
        }
        return true;
        
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

    private boolean checkIsPlayerWon() {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer player = iter.next();
            if(player.getScore() >= Constants.HANGMAN_MAX_SCORE) {
                cancelReadyTimer();
                ServerPlayer winningPlayer = getHighScorePlayer(turnOrder);
                sendMessage(null,winningPlayer.getClient().getClientName() + " won the game with the max score of " + winningPlayer.getScore());
                resetSession();
                return true;
            }
        }
        return false;     
    }

    private boolean checkIsGameCompleted() {
        if(game.getIsGameCompleted()){
            ServerPlayer winningPlayer = getHighScorePlayer(turnOrder);
            sendMessage(null, "Game Ended " + winningPlayer.getClient().getClientName() + " won with a score of " + winningPlayer.getScore());
            resetSession();
            return true;
        }
        return false;
    }

    private void checkIsRoundCompleted() {
        if(game.isBlankCompleted()){
            sendMessage(null, "Blank Word Solved! The word was " + game.getCurrentWord());
            displayPlayersScoreRanked(turnOrder);
            if(!checkIsGameCompleted()); {
                if(game.canGoToNextRound()){
                announceRound();
                }
            }
        }
        if(game.isHangmanCompleted()){
            sendMessage(null, "Hangman Completed.... The word was " + game.getCurrentWord());
            displayPlayersScoreRanked(turnOrder);
            if(!checkIsGameCompleted()){
                if(game.canGoToNextRound()) {
                    announceRound();
                }
            }   
        }
    }

    //getters

    private ServerPlayer getHighScorePlayer(List<ServerPlayer> players) {
        Iterator<ServerPlayer> iter = players.iterator();
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