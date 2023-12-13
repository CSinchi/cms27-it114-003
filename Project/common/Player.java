package Project.common;

//Added from Ready Check     Cristian Sinchi cms27
public class Player {

    private boolean isReady = false;
    private boolean wantsHardMode = false;
    private boolean wantsForgiveOp = false;
    private boolean isSpectating = true;

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return this.isReady;
    }

    public void setWantHardMode(boolean wantsHardMode) {
        this.wantsHardMode = wantsHardMode;
    }

    public boolean wantsHardMode() {
        return wantsHardMode;
    }

    public void setForgiveOP(boolean wantsForgiveOp) {
        this.wantsForgiveOp = wantsForgiveOp;
    }

    public boolean wantsForgiveOp() {
        return wantsForgiveOp;
    }

    public void setIsSpectating(boolean isSpectating) {
        this.isSpectating = isSpectating;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

}