package Project.common;

//Added from Ready Check     Cristian Sinchi cms27
public class Player {

    private boolean isReady = false;
    private boolean wantsHardMode = false;
    private boolean wantsForgiveOp = false;

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

}