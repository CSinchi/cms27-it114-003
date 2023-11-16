package Project.server;

//Cristian Sinchi cms27

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import Project.common.Constants;

public class HangmanGame {

    private ArrayList<String> WordsList = new ArrayList<>(Arrays.asList(Constants.HANGMAN_DEFAULT_WORDLIST));//setting up modifiable word list
    private Iterator<String> currentWordListIter;
    private String currentWord; //stores current word to be guessed
    private char[] blankCurrentWordArr; //stores an array of blanks that can get filled in and be getted (be string)
    
    private int currentRound; //stores current round number
    private int hangManStrikes; //default strikes should always begin with zero
 
    private boolean isGameRunning = false; //boolean if game is running
    private boolean isGameCompleted = false;
    private boolean isRoundFinished;    

    public HangmanGame() {
        isGameRunning = true; // Resetting/Setting game data
        currentRound = 1;
        shuffleList(WordsList);
        currentWordListIter = WordsList.iterator();
        setGameRound(currentWordListIter);

    }

    private void setGameRound(Iterator<String> iterator) {  //Sets up game round.
        if(iterator.hasNext()){
            hangManStrikes = 0;
            currentWord = iterator.next();
            blankCurrentWordArr = createBlankArr();//sets up blank current word arr
            isRoundFinished = false;
        }
        else{
            isGameCompleted = true;
        }
        
    }

    private void shuffleList(ArrayList<String> s){ //method to shuffle an ArrayList<String> in this case the word list order
        Collections.shuffle(s);
    }

    //Blank array methods

    private char[] createBlankArr() {  //creates a char array with the size of the current word's length with underscores each element
        char[] blank = new char[currentWord.length()];
        for(int i = 0; i < currentWord.length(); i++){
            blank[i] = '_';
        }
        return blank;
    }

    private void fillBlankArr(char[] blankArr, char g) { //edits a char array to replace blank spots with chars that match with the current word
        char[] explodedCurrentWord = currentWord.toCharArray();
        for(int i = 0; i < explodedCurrentWord.length; i++){
            if(g == explodedCurrentWord[i]){
                blankArr[i] = g;
            }
        }
    }

    //Guesses methods

    protected boolean isLetterCorrect(char guess) { //Returns true if letter guess was right and vice versa. Other classes should be able to use this method
        char[] explodedCurrentWord = currentWord.toCharArray();
        for (int i = 0; i < explodedCurrentWord.length; i++ ){
            if(guess == explodedCurrentWord[i]){
                fillBlankArr(blankCurrentWordArr, guess); //fills in the letters in the blank word
                return true;
            }
        }
        hangManStrikes++;
        return false;
    }

    protected boolean isWordCorrect(String guess) { //Returns true if word guess was right and vice versa. Other classes should be able to use this method
        if (guess.equals(currentWord)) {
            return true;
        }
        hangManStrikes++;
        return false;
    }
    
    protected int guessedLettersScore(char guess){ //return the score the player earned. Also modifies guessed word int (Must use with isLetterCorrect in GameRoom)
        char[] explodedCurrentWord = currentWord.toCharArray();
        int amount = 0;
        for (int i = 0; i < explodedCurrentWord.length; i++){
            if(guess == explodedCurrentWord[i]){
                amount++;
            }
        }
        return amount*Constants.HANGMAN_DEFAULT_SCORE;
    }

    protected int guessedWordScore(String guess) { //return the score the player earned (Must use with isWordCorrect in GameRoom)
        int amount = 0;
        for(int i = 0; i < blankCurrentWordArr.length; i++) {//score is based on how many blanks were remaining (more blanks = more points)
            if (blankCurrentWordArr[i] == '_'){
                amount++;
            }
        }
        blankCurrentWordArr = guess.toCharArray();//fills up the blank word when done
        return amount * Constants.HANGMAN_DEFAULT_SCORE;
    }

    //game booleans

    protected boolean isHangmanCompleted (){
        if (hangManStrikes >= Constants.HANGMAN_MAX_STRIKES){
            isRoundFinished = true;
            return true;
        }
        return false;
    }

    protected boolean isBlankCompleted() {
        for (int i = 0; i < blankCurrentWordArr.length; i++){
            if (blankCurrentWordArr[i] == '_'){
                return false;
            }
        }
        isRoundFinished = true;
        return true;
    }
    protected boolean canGoToNextRound(){ //dependant on hangmancompleted and isBlankCompleted
        if(isRoundFinished){
            currentRound++;
            setGameRound(currentWordListIter);
            return true;
        }
        return false;
    }

    //getters

    protected int getCurrentRound() {
        return currentRound;
    }

    protected int getHangmanStrikes(){
        return hangManStrikes;
    }

    protected boolean getIsGameCompleted() {
        return isGameCompleted;
    }

    protected String getCurrentWord() {
        return currentWord;
    }

    protected String getBlankStr() { //returns a string of the current blank current word array
        StringBuilder sb = new StringBuilder(32);
        for(int i = 0; i < blankCurrentWordArr.length; i++){
            sb.append(blankCurrentWordArr[i] + " ");
        }
        return sb.toString().trim();
    }



    

    
}
