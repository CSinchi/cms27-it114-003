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
        logger.info(TextFX.colorize("Restart Session Triggered", Color.PURPLE));
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
            turnOrder.remove(player); //remove client from turnOrder
            super.handleDisconnect(null, player.getClient()); 
            logger.info(String.format("Total clients %s", clients.size()));
            sendMessage(null, player.getClient().getClientName() + " disconnected");
            if(players.size() < Constants.MINIMUM_PLAYERS) {
                cancelReadyTimer(); //cancel turns
                sendMessage(null,"Not enough players in the game. Restarting session");
                resetSession();    
            }
            if (players.isEmpty()) {
                cancelReadyTimer();//cancel turns
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

    protected void announceRound() { //cms27 12/14/2023
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

    private void displayPlayersScoreRanked(List<ServerPlayer> players){ //  cms27 11/15/2023
        List<ServerPlayer> rankedList = new ArrayList<>(players); //create a list from the existing "players" list
        rankedList.sort(Comparator.comparing(ServerPlayer::getScore).reversed()); //sorts this list based on the value of every player's score (sorts from greatest to least)
        StringBuilder sb = new StringBuilder(100); //create a string builder
        Iterator<ServerPlayer> iter = rankedList.iterator();//create a iterator to go through the ranked list
        int rankNum = 1; //placement num, will change after each iteration
        while(iter.hasNext()){
            sb.append(" ");
            ServerPlayer player = iter.next(); //gets a player from rankedList
            String s = String.format("[%d] %s-%d", rankNum, player.getClient().getClientName(),player.getScore()); 
            sb.append(s);
            rankNum++;

        }
        sendMessage(null, "Score Rankings:"+ sb);

    }

    //guessing handling methods cms27 11/13/2023

    protected void handleGuessLetter(String guess, ServerThread client) { //cms27 11/14/2023
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){ //sends a msg back if its not the player's turn (compares from currentTurnPlayer )
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "It is not your turn yet");
            return;
        }
        if(currentPhase != Phase.TURN){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot guess outside of a turn"); //sends a msg back if trying to guess outside of TURN
            return;
        }
        if (!hasLetters(guess)){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot send a non letter guess"); //sends a msg back if the guess is not a letter
            return;
        }
        char letter = guess.charAt(0);
        sendMessage(null, client.getClientName() + " guessed the letter " + letter); 
        if (game.isLetterCorrect(letter)){
            cancelReadyTimer(); //stops any timer
            sendMessage(null, client.getClientName() + " got the letter right!");
            scorePlayer(currentTurnPlayer, game.guessedLettersScore(letter));//scores the player
                if (!checkIsPlayerWon()) {//checks if the max score win condition has been achieved
                sendMessage(null, "New Blank Word: " + game.getBlankStr());  //Sends out a new blank to clients
                checkIsRoundCompleted(); //goes to next round if applicable (blank word is completed)
                    if(!checkIsGameCompleted()){ //goes to the next turn unless if the game is finished (bool from isGameCompleted)
                        nextTurn();
                    } 
                }
            }
        else{
            cancelReadyTimer(); //stops any timer
            sendMessage(null, client.getClientName() + " got the letter wrong!");
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            checkIsRoundCompleted(); //goes to next round if applicable (hangman completed)
            if(!checkIsGameCompleted()){//goes to the next turn unless if the game is finished (bool from isGameCompleted)
                nextTurn();
            }
        }
    }

    protected void handleGuessWord(String guess, ServerThread client) { //cms27 11/14/2023
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "It is not your turn yet"); //sends a msg back if its not the player's turn (compares from currentTurnPlayer)
            return;
        }
        if(currentPhase != Phase.TURN){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot guess outside of a turn"); //sends a msg back if trying to guess outside of TURN
            return;
        }
        if (!hasLetters(guess)){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot send a non letter guess"); //sends a msg back if the guess has a non letter character
            return;
        }
        sendMessage(null, client.getClientName() + " guessd the word " + guess);
        if (game.isWordCorrect(guess)){
            cancelReadyTimer();//stops any timer
            sendMessage(null, client.getClientName() + " got the word right!");
            scorePlayer(currentTurnPlayer, game.guessedWordScore(guess));//scores the player
                if(!checkIsPlayerWon()){//checks if the max score win condition has been achieved
                    sendMessage(null, "New Blank Word: " + game.getBlankStr());  //Sends out a new blank to clients (in this case a completed blank)
                    checkIsRoundCompleted();//goes to next round if applicable (blank completed)
                    if(!checkIsGameCompleted()){//goes to the next turn unless if the game is finished (bool from isGameCompleted)
                        nextTurn();
                    }
                }  
        }   

        else {
            cancelReadyTimer();//stops any timer
            sendMessage(null, client.getClientName() + " got the word wrong!");
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            checkIsRoundCompleted(); //goes to next round if applicable (hangman completed)
            if(!checkIsGameCompleted()){ //goes to the next turn unless if the game is finished (bool from isGameCompleted)
                nextTurn();
            }
        }
    }

    protected void handleSkip(ServerThread client) {
        if(client.getClientId() != currentTurnPlayer.getClient().getClientId()){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot skip a turn that is not yours");
            return;
        }
        if(currentPhase != Phase.TURN){
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot skip outside of a turn");
            return;
        }
        cancelReadyTimer();
        sendMessage(null,client.getClientName()+" skipped thier turned!");
        nextTurn();
    }

     public boolean hasLetters (String str) { //function that will return false if a character in the string is not a letter    cms27 11/16/2023
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;  
    }


    //turn methods from dungeon prep Project    Cristain Sinchi cms27 11/15/2023

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
                        String.format("%s took too long and has been skipped", sp.getClient().getClientName()));
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
                logger.info(TextFX.colorize(winningPlayer.getClient().getClientName() + " achieved win condition-> MAX Score reached or suprass", Color.PURPLE));
                sendMessage(null,"MAX Score Hit!!! " + winningPlayer.getClient().getClientName() + " won the game with the score of " + winningPlayer.getScore());
                resetSession();
                return true;
            }
        }
        return false;     
    }

    private boolean checkIsGameCompleted() { //this boolean is used in guess handling to check for game completion      cms27 11/13/2023
        if(game.getIsGameCompleted()){ //Checks boolean IsGameCompletd in hangman obj (if true, then game is finshed)
            ServerPlayer winningPlayer = getHighScorePlayer(turnOrder); //gets the player with the highest score
            logger.info(TextFX.colorize(winningPlayer.getClient().getClientName() + " achieved win condition-> Completed game with highest score", Color.PURPLE));
            sendMessage(null, "Game Ended " + winningPlayer.getClient().getClientName() + " won with a score of " + winningPlayer.getScore());
            resetSession(); //goes back to READY phase
            return true;
        }
        return false;
    }

    private void checkIsRoundCompleted() {// this boolean is used in guess handling to check if the next round can be go to
        if(game.isBlankCompleted()){ //checks boolean in hangman obj (if true then broadcast win round) Note: blank word gets completed if a word guess was true
            sendMessage(null, "Blank Word Solved! The word was " + game.getCurrentWord());
            displayPlayersScoreRanked(turnOrder); //function to display player scores
            if(!checkIsGameCompleted()); { //runs if game is not completed
                if(game.canGoToNextRound()){
                announceRound();
                }
            }
        }
        if(game.isHangmanCompleted()){ //checks boolean in hangman obj (if true then broadcast lose round)
            sendMessage(null, "Hangman Completed.... The word was " + game.getCurrentWord());
            displayPlayersScoreRanked(turnOrder);//function to display player scores
            if(!checkIsGameCompleted()){ //runs if game is not completed
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