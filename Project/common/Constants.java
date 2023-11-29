package Project.common;

import Project.server.HangmanGame;

public abstract class Constants {
    public static final long DEFAULT_CLIENT_ID = -1L;
    public static final int MINIMUM_PLAYERS = 2;
    public static final String LOBBY = "lobby";

    public static final int HANGMAN_DEFAULT_SCORE = 1; //Constants for Hangman Game Object      Cristian Sinchi cms27 11/13/2023
    public static final int HANGMAN_MAX_SCORE = 20;
    public static final int HANGMAN_MAX_STRIKES = 6;
    public static final int HANGMAN_MAX_ROUNDS = 5;
    public static final String[] HANGMAN_DEFAULT_WORDLIST = {"elevator", "courage", "currency", "manager", "library", "homework", "liability", "protection", "vigorous", "tumble",
    "despair", "miracle", "barrier", "authority", "ancestor", "glance", "uniform", "infinite", "bronze" , "imposter" , "student", "gravity" , "reaction", "falsify"};
}