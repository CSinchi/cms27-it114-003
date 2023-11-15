package Project.common;

//Cristian Sinchi cms27

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import Project.common.Constants;

public class HangmanGame {

    private ArrayList<String> WordsList = new ArrayList<>(Arrays.asList(Constants.HANGMAN_DEFAULT_WORDLIST));//setting up modifiable word list
    private String currentWord; //stores current word to be guessed
    private int lettersGuessedInt; //stores the amount of letters guess in a word (in total)
    private char[] blankCurrentWordArr; //stores an array of blanks that can get filled in and be getted (be string)
    
    private int totalRounds; //default amount of rounds usually = length of wordslist
    private int currentRound; //stores current round number
    private int hangManStrikes; //default strikes should always begin with zero
 
    private boolean isGameRunning = false; //boolean if game is running
    private boolean isGameCompleted = false; //boolean if game is completed
    private boolean isGuessRight = false; //boolean if a guess was correct
    private String prevGameState;
    

    public HangmanGame() {

    }

    protected void startGame(){ // Startup method for this code. Gameroom should be able to run this object in its class
        isGameRunning = true; // Resetting/Setting game data
        isGameCompleted = false;
        prevGameState = "none";
        currentRound = 1;
        totalRounds = WordsList.size();
        shuffleList(WordsList);
        for(int i = 0; i < totalRounds; i++ ){  //Runs serveral rounds based on totalRounds' value
            gameRound(WordsList.get(i)); //start new round with current element string from wordlist
            currentRound++; //when gameRound breaks, set the next round int
            if(isGameCompleted) { //game will stop when the isGameCompleted condition is true
                break;
            }
        }
        isGameRunning = false; //game no longer is running
    }

    private void gameRound(String word){ //Starts up game round. Can only be runned by startGame()
        currentWord = word; //update word to be guessed
        blankCurrentWordArr = createBlankArr(); //generate blank word
        hangManStrikes = 0;  //reset strikes

        while(true) {  //will keep going until one of the bools below is true;
            if(isGuessRight && lettersGuessedInt >= currentWord.length()) { //Complete Win (also check if all letters has been guessed)
                prevGameState = "win";
                break;
            }
            if(hangManStrikes >= 5){
                prevGameState = "loss"; //Hanged Man Loss
                break;
            }
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

    protected String getBlankStr() { //returns a string of the current blank current word array
        StringBuilder sb = new StringBuilder(32);
        for(int i = 0; i < blankCurrentWordArr.length; i++){
            sb.append(blankCurrentWordArr[i] + " ");
        }
        return sb.toString().trim();
    }

    //Guesses methods

    protected boolean isLetterCorrect(char guess) { //Returns true if letter guess was right and vice versa. Other classes should be able to use this method
        char[] explodedCurrentWord = currentWord.toCharArray();
        for (int i = 0; i < explodedCurrentWord.length; i++ ){
            if(guess == explodedCurrentWord[i]){
                isGuessRight = true; //change state
                fillBlankArr(blankCurrentWordArr, guess); //fills in the letters in the blank word
                return true;
            }
        }
        hangManStrikes++;
        isGuessRight = false;
        return false;
    }

    protected boolean isWordCorrect(String guess) { //Returns true if word guess was right and vice versa. Other classes should be able to use this method
        if (guess == currentWord){
            isGuessRight = true;
            return true;
        }
        hangManStrikes++;
        isGuessRight = false;
        return false;
    }
    
    protected int guessedLettersScore(char guess){ //return the score the player earned. Also modifies guessed word int (Must use with isLetterCorrect)
        char[] explodedCurrentWord = currentWord.toCharArray();
        int amount = 0;
        for (int i = 0; i < explodedCurrentWord.length; i++){
            if(guess == explodedCurrentWord[i]){
                amount++;
            }
        }
        lettersGuessedInt += amount; //Adds amount of letters guess to lettersInt
        return amount*Constants.HANGMAN_DEFAULT_SCORE;
    }

    protected int guessedWordScore(String guess) { //return the score the player earned. Also fills guessed word int(auto winround) (Must use with isWordCorrect)
        int amount = 0;
        if(guess == currentWord){
            for(int i = 0; i < blankCurrentWordArr.length; i++) {//score is based on how many blanks were remaining (more blanks = more points)
                if (blankCurrentWordArr[i] == '_'){
                    amount++;
                }
            }
            return amount * Constants.HANGMAN_DEFAULT_SCORE;
        }
        else {
            return amount;
        }
    }

    

    
}
