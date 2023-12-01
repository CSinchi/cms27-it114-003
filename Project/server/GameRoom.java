package Project.server;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<ServerPlayer> preTurnOrder = new ArrayList<ServerPlayer>();
    private List<ServerPlayer> turnOrder;
    private List<Character> guessedLetters = new ArrayList<Character>();

    public GameRoom(String name) {
        super(name);
        
    }

    private void syncGameState(ServerThread incomingClient) {  //from Drawing Grid
        if (currentTurnPlayer != null) {
            incomingClient
                    .sendCurrentTurn(incomingClient.getClientId());
        }
        incomingClient.sendPhaseSync(currentPhase);
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            if (client.getClient().getClientId() == incomingClient.getClientId()) {
                continue;
            }
            boolean success = false;
            if (client.isReady()) {
                success = incomingClient.sendReadyStatus(client.getClient().getClientId());
            }

            if (!success) {
                break;
            }
        }
    }

    @Override
    protected void addClient(ServerThread client) {
        logger.info(TextFX.colorize("Adding client as player",Color.BLUE));
        players.computeIfAbsent(client.getClientId(), id -> {
            ServerPlayer player = new ServerPlayer(client);
            super.addClient(client);
            syncGameState(client);
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
        logger.info(TextFX.colorize("Game Initializing", Color.PURPLE));
        preTurnOrder = players.values().stream().filter(ServerPlayer::isReady).toList(); //initalize turnOrderList
        shuffleServerPlayers();//shuffle order
        players.values().stream().forEach(p -> p.setScore(0)); //Sets up player values
        players.values().stream().forEach(p-> p.setPlacement(0));
        syncRankedPlayers(turnOrder);
        game = new HangmanGame();
        sendMessage(null, "Started Hangman Game");
        announceRound();
        logger.info(TextFX.colorize("nextTurn invoked from start()", Color.YELLOW));
        nextTurn();
        
        }

    private synchronized void resetSession() {
        logger.info(TextFX.colorize("Restart Session Triggered", Color.PURPLE));
        players.values().stream().forEach(p -> p.setReady(false));
        players.values().stream().forEach(p -> p.setScore(0)); //addition to resetSession to also clear out players data when called
        players.values().stream().forEach(p-> p.setPlacement(0));
        guessedLetters.clear();
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
            rankPlayers(turnOrder); //refresh rankings 
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
    //New syncs for sending data to client ui   cms27 11/26/23

    private void syncTimer(int time) {  //sends TIME payload to clients in room
        String s = String.valueOf(time);
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while(iter.hasNext()){
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendTimer(s);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    private void syncBlankWord(String word) { //sends BLANK_WORD payload to clients in room
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while(iter.hasNext()){
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendBlankWord(word);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    private void syncRound(int round) {  //sends ROUND payload to clients in room
        String r = String.valueOf(round);
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while(iter.hasNext()){
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendRound(r);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    private void syncStrike(int strike) {  //sends STRIKE paylaod to clients in room
        String s = String.valueOf(strike);
        Iterator<ServerPlayer> iter  =players.values().stream().iterator();
        while(iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendStrike(s);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    private void syncLetterStat(String letter , Boolean isCorrect) {  //sends LETTERSTAT paylaod to clients in room
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while(iter.hasNext()){
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendLetterStat(letter, isCorrect);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    private void syncRankedPlayers (List<ServerPlayer> players) {
        String[] rs = new String[players.size()];
        int index = 0;
        Iterator<ServerPlayer> rankedIter = players.iterator();
        while(rankedIter.hasNext()){
            ServerPlayer rankedPlayer = rankedIter.next();
            if (index < rs.length && rankedPlayer.getPlacement() != 0) {
                rs[index] = String.format("%d. %s[%d]", rankedPlayer.getPlacement(),rankedPlayer.getClient().getClientName(), rankedPlayer.getScore());
            } else {
                rs[index] = String.format("%s[%d]",rankedPlayer.getClient().getClientName(), rankedPlayer.getScore());
            }
            index++;
        }

        Iterator<ServerPlayer> iter = this.players.values().stream().iterator();
        while(iter.hasNext()){
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendRankedPlayers(rs);
            if(!success) {
                handleDisconnect(client);
            }
        }
    }

    protected void announceRound() { //cms27 12/14/2023
        logger.info(String.format(TextFX.colorize("This Round [%d] word is %s",Color.PURPLE),game.getCurrentRound(),game.getCurrentWord()));
        sendMessage(null, "Round " + game.getCurrentRound() + " Blank Word: " + game.getBlankStr());
        syncRound(game.getCurrentRound());
        syncStrike(game.getHangmanStrikes());
        syncBlankWord(game.getBlankStr());
    }

    protected void announceNextRound() {
        if (game.getIsGameCompleted()) {
            sendMessage(null, "MAX rounds reached! Game Ending soon!");
        }
        else {
            sendMessage(null, "Next Round will start soon!");
        }
    }

    //Serverplayer methods

    private void scorePlayer(ServerPlayer player, int score) {  //score handling method cms27 11/15/2023
        player.addScore(score);
        logger.info(String.format(TextFX.colorize("%s scored %d points", Color.PURPLE),player.getClient().getClientName(), score));
        sendMessage(null, String.format("%s scored %d points!",player.getClient().getClientName(), score));
        rankPlayers(turnOrder); //reevaluates rankings
        //sendMessage(null, String.format("&s's total score is %d points", player.getClient().getClientName(), player.getScore()));
      }

    
    private void rankPlayers(List<ServerPlayer> players) { 
        logger.info(TextFX.colorize("Ranking Players", Color.PURPLE));
        List<ServerPlayer> rankedPlayers = new ArrayList<>(players); //creates new list
        rankedPlayers.sort(Comparator.comparing(ServerPlayer::getScore).reversed()); //orders list based on score values (higher one are first)
        Iterator<ServerPlayer> iter = rankedPlayers.iterator();
        int rankNum = 1;
        Boolean allScoresZeros = rankedPlayers.stream().allMatch(r -> r.getScore() == 0); //is true when no one has any points
        while(iter.hasNext()){
            ServerPlayer player = iter.next();
            if (!allScoresZeros) { //will set the placement unless no one has points
                player.setPlacement(rankNum);
                rankNum++;
            } else {
                break;
            }
            
        }
        syncRankedPlayers(rankedPlayers);
    }

    private void displayPlayersScoreRanked() { //  cms27 11/15/2023
        List<ServerPlayer> rankedList = new ArrayList<>(turnOrder); //create a list from the existing "players" list
        rankedList.sort(Comparator.comparing(ServerPlayer::getScore).reversed()); //sorts this list based on the value of every player's score (sorts from greatest to least)
        StringBuilder sb = new StringBuilder(100); //create a string builder
        Iterator<ServerPlayer> iter = rankedList.iterator();//create a iterator to go through the ranked list
        int rankNum = 1; //placement num, will change after each iteration
        while(iter.hasNext()){
            sb.append(" ");
            ServerPlayer player = iter.next(); //gets a player from rankedList
            String s = String.format("%d. %s[%d]", rankNum, player.getClient().getClientName(),player.getScore()); 
            sb.append(s);
            rankNum++;

        }
        sendMessage(null, "Score Rankings:"+ sb);

    }

    //guessing handling methods cms27 11/13/2023 reworked on 11/29/23

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
        Character Letter = letter;
        if (guessedLetters.contains(Letter)) {
            client.sendMessage(Constants.DEFAULT_CLIENT_ID, "You cannot send an already guessed letter");//sends a msg back if they guess was already made
            return;
        } else {
            guessedLetters.add(Letter); //adds the letter to the guessList if otherwise
        }
        sendMessage(null, client.getClientName() + " guessed the letter " + letter); 
        if (game.isLetterCorrect(letter)){
            cancelReadyTimer(); //stops any timer
            sendMessage(null, client.getClientName() + " got the letter right!");
            syncLetterStat(guess,true);
            scorePlayer(currentTurnPlayer, game.guessedLettersScore(letter));//scores the player
            if (!checkIsPlayerWon()) {//checks if the max score win condition has been achieved
                sendMessage(null, "New Blank Word: " + game.getBlankStr());  //Sends out a new blank to clients
                syncBlankWord(game.getBlankStr());
                checkIsRoundCompleted("guessLetter right"); //goes to next round if applicable (blank word is completed) 
            }
        }
        else {
            cancelReadyTimer(); //stops any timer
            sendMessage(null, client.getClientName() + " got the letter wrong!");
            syncLetterStat(guess, false);
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            syncStrike(game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            syncBlankWord(game.getBlankStr());
            checkIsRoundCompleted("guessLetter wrong"); //goes to next round if applicable (hangman completed)
        }
    }

    protected void handleGuessWord(String guess, ServerThread client) { //cms27 11/14/2023 reworked on 11/29/23
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
                    syncBlankWord(game.getBlankStr());
                    checkIsRoundCompleted("guessWord right");//goes to next round if applicable (blank completed)
                }  
        }   

        else {
            cancelReadyTimer();//stops any timer
            sendMessage(null, client.getClientName() + " got the word wrong!");
            sendMessage(null, "Strikes:" + game.getHangmanStrikes());
            syncStrike(game.getHangmanStrikes());
            sendMessage(null, "Blank Word: " + game.getBlankStr());
            syncBlankWord(game.getBlankStr());
            checkIsRoundCompleted("guessWord wrong"); //goes to next round if applicable (hangman completed)

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
        logger.info(TextFX.colorize("nextTurn invoked from normal skip", Color.YELLOW));
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


    //turn methods from dungeon prep Project in addtion to new methods    Cristain Sinchi cms27 11/15/2023  11/29/23

    private void nextTurn() { //used to go to next player
        logger.info(String.format(TextFX.colorize("This Round [%d] word is %s",Color.PURPLE),game.getCurrentRound(),game.getCurrentWord())); //debugging for word guessing
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
            readyTimer = new TimedEvent(30, () -> { //Will proceed if server does not recive any guesses or manual skips from currentTurn client (auto skip)
                logger.info(TextFX.colorize("nextTurn invoked from auto skip", Color.YELLOW));
                sendMessage(null,
                        String.format("%s took too long and has been skipped", sp.getClient().getClientName()));
                nextTurn(); 
            });
            readyTimer.setTickCallback((time)->{syncTimer(time);}); //sends TIME payloads to clients for each second above timer has passed
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

    private void shuffleServerPlayers() { //used during start()
        List<ServerPlayer> sList = new ArrayList<>(preTurnOrder);
        Collections.shuffle(sList);
        turnOrder = new ArrayList<>(sList);
    }

    //void check methods for complete win, win round, or lose round

    private boolean checkIsPlayerWon() {
        logger.info(TextFX.colorize("Game Complete Check invoked", Color.YELLOW));
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer player = iter.next();
            if(player.getScore() >= Constants.HANGMAN_MAX_SCORE) {
                cancelReadyTimer();
                updatePhase(Phase.RESOLVE);
                ServerPlayer winningPlayer = getHighScorePlayer(turnOrder);
                logger.info(TextFX.colorize(winningPlayer.getClient().getClientName() + " achieved win condition-> MAX Score reached or suprass", Color.PURPLE));
                sendMessage(null,"MAX Score Hit!!! " + winningPlayer.getClient().getClientName() + " won the game with the score of " + winningPlayer.getScore());
                resetSession();
                return true;
            }
        }
        return false;     
    }

    private void checkIsGameCompleted() { //this boolean is used in guess handling to check for game completion   cms27 11/13/2023  reworked 11/29/23
        logger.info(TextFX.colorize("Game Complete Check invoked", Color.YELLOW));
        if(game.getIsGameCompleted()){ //Checks boolean IsGameCompletd in hangman obj (if true, then game is finshed)
            cancelReadyTimer(); 
            updatePhase(Phase.RESOLVE);
            ServerPlayer winningPlayer = getHighScorePlayer(turnOrder); //gets the player with the highest score
            logger.info(TextFX.colorize(winningPlayer.getClient().getClientName() + " achieved win condition-> Completed game with highest score", Color.PURPLE));
            sendMessage(null, "Game Ended " + winningPlayer.getClient().getClientName() + " won with a score of " + winningPlayer.getScore());
            resetSession();
        }
    }

    private void checkIsRoundCompleted(String originInvoke) {// this boolean is used in guess handling to check if the next round can be proceded   cms27   reworked 11/29/23
        if(game.isBlankCompleted()){ //checks boolean in hangman obj (if true then broadcast win round) Note: blank word gets completed if a word guess was true
            updatePhase(Phase.RESOLVE);
            sendMessage(null, "Blank Word Solved! The word was " + game.getCurrentWord());
            displayPlayersScoreRanked(); //function to display player scores
            announceNextRound();
            logger.info(TextFX.colorize("Game Check Delay Begin", Color.YELLOW));
            readyTimer = new TimedEvent(6, () -> { //setting a delay until new round is evaluated
                logger.info(TextFX.colorize("Game Check Delay End", Color.YELLOW));
                checkIsGameCompleted();
                if(game.canGoToNextRound()) { //will go to next round if it can and when the game is not complete
                    guessedLetters.clear();
                    announceRound();
                    logger.info(TextFX.colorize("nextTurn invoked for new round from " + originInvoke, Color.YELLOW));
                    nextTurn();
                }
            });
            
        }
    
        else if(game.isHangmanCompleted()){ //checks boolean in hangman obj (if true then broadcast lose round)
            sendMessage(null, "Hangman Completed.... The word was " + game.getCurrentWord());
            displayPlayersScoreRanked();//function to display player scores
            announceNextRound();
            logger.info(TextFX.colorize("Game Check Delay Begin", Color.YELLOW));
            readyTimer = new TimedEvent(6, () -> { //setting a delay until new round is evaluated
                logger.info(TextFX.colorize("Game Check Delay End", Color.YELLOW));
                checkIsGameCompleted();
                if(game.canGoToNextRound()) { //will go to next round if it can and when the game is not complete
                    guessedLetters.clear();
                    announceRound();
                    logger.info(TextFX.colorize("nextTurn invoked for new round from " + originInvoke, Color.YELLOW));
                    nextTurn();
                }
            });
        } 
        
        else { //goes to the next turn 
            logger.info(TextFX.colorize("nextTurn invoked from " + originInvoke, Color.YELLOW)); //should only send "wrong" origins
            nextTurn();
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