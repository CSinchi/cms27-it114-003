package Project.client;

import Project.common.Phase;

public interface IGameEvents extends IClientEvents {
    /**
     * Triggered when a player marks themselves ready
     * 
     * @param clientId Use -1 to reset the list
     */
    void onReceiveReady(long clientId);

    /**
     * Triggered when client receives phase update from server
     * 
     * @param phase
     */
    void onReceivePhase(Phase phase);

    //Triggered when  client recives a turn update from server
    void onReceiveTurn(String player);

    //Triggered when client recives time update from server
    void onReceiveTime(String time);

    //Triggered when client recives blank word from server
    void onReceiveBlankWord(String word);

    void onReceiveLetterStat(String letter, Boolean isCorrect);

    void onReceiveRound(String round);

    void onReceiveRankedPlayers(String[] players);

    void onReceiveStrike(String strike);
}
