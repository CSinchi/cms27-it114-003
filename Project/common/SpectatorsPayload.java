package Project.common;

public class SpectatorsPayload extends Payload {
    private String[] spectators;

    public SpectatorsPayload () {
        super();
        setPayloadType(PayloadType.SPECTATOR);
    }

    public String[] getSpectators() {
        return spectators;
    }

    public void setSpectators(String[] spectators) {
        this.spectators = spectators;
    }
}
