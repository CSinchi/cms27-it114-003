package Project.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameRoom extends Room {

    private final String[] defaultWordArray = {"elevator", "courage", "currency", "manager", "library", "homework"};
    private int totalRounds; //default amount of rounds
    private final int letterGuessPoint = 1; //Point earned for guessing a letter
    private int wordGuessPoint = 5; //Points that can be earned by guessing a word (amount earned will vary)
    private ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(defaultWordArray));

    public GameRoom(String name) {
        super(name);
        
    }

    private void shuffleList(ArrayList<String> s){
        Collections.shuffle(s);
    }

}