package Project.server;
import Project.common.Player;

//Added from Ready Check     Cristian Sinchi cms27

public class ServerPlayer extends Player {
    private ServerThread client;
    private int score;
    private int placement;

    public void setClient(ServerThread client) {
        this.client = client;
    }

    public ServerThread getClient() {
        return this.client;
    }

    public void setScore (int score) { //new data for serverplayer scores
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void addScore (int score) { 
        this.score += score;
    }
    
    public void setPlacement(int placement){
        this.placement = placement;
    }

    public int getPlacement(){
        return placement;
    }
    public ServerPlayer(ServerThread client) {
        setClient(client);
    }
}