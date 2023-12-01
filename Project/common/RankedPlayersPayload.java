package Project.common;

public class RankedPlayersPayload extends Payload {
    private String[] players;

    public RankedPlayersPayload () {
        super();
        setPayloadType(PayloadType.RANKED_PLAYERS);
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }
}
