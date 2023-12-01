package Project.common;

public class LetterStatPayload extends Payload{
    private boolean isCorrect;
    
    public LetterStatPayload() {
        super();
        setPayloadType(PayloadType.LETTER_STAT);
    }

    public void setStat(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public boolean getStat() {
        return isCorrect;
    }
}
