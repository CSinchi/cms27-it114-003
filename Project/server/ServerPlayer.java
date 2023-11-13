package Project.server;
import Project.common.Player;

//Added from Ready Check     Cristian Sinchi cms27

public class ServerPlayer extends Player {
    private ServerThread client;

    public void setClient(ServerThread client) {
        this.client = client;
    }

    public ServerThread getClient() {
        return this.client;
    }

    public ServerPlayer(ServerThread client) {
        setClient(client);
    }
}