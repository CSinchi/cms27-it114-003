package Project.common;



public abstract class Constants {
    public static final long DEFAULT_CLIENT_ID = -1L;
    public static final int MINIMUM_PLAYERS = 2;
    public static final String LOBBY = "lobby";

    public static final int HANGMAN_DEFAULT_SCORE = 1; //Constants for Hangman Game Object      Cristian Sinchi cms27 11/13/2023    updated 12/12/2023
    public static final int HANGMAN_MAX_SCORE = 20;
    public static final int HANGMAN_MAX_STRIKES = 6;
    public static final int HANGMAN_MAX_ROUNDS = 5;
    public static final char[] HANGMAN_EXTRA_POINTS_LETTER_LIST = {'m', 'h', 'g', 'b', 'f', 'y', 'w', 'k', 'v', 'x', 'z', 'j', 'q'};
    public static final String[] HANGMAN_DEFAULT_WORDLIST = {"elevator", "courage", "currency", "manager", "library", "homework", "liability", "protection", "vigorous", "tumble",
    "despair", "miracle", "barrier", "authority", "ancestor", "glance", "uniform", "infinite", "bronze" , "imposter" , "student", "gravity" , "reaction", "falsify",
    "obstacle", "treasure", "crosswalk", "equinox", "withdraw", "knowledge", "hemisphere", "photograph", "judgement", "variable", "magjesty", "pajama"};
}