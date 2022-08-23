public class MineLocation {
    private boolean mine;
    private boolean uncovered;
    MineLocation(boolean mine, boolean uncovered) {
        this.mine = mine;
        this.uncovered = uncovered;
    }
    public void setMine(boolean mineVal) {
        this.mine = mineVal;
    }
    public boolean getMine() {
        return this.mine;
    }
    public void setUncovered(boolean uncoVal) {
        this.uncovered = uncoVal;
    }
    public boolean getUncovered() {
        return this.uncovered;
    }
}
