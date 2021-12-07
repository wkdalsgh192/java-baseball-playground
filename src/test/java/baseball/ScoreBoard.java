package baseball;

class ScoreBoard {

    private final static int GAME_OVER = 3;
    private int ballCnt;
    private int strikeCnt;
    private boolean nothing = false;

    public int getBallCnt() { return ballCnt; }

    public int getStrikeCnt() {
        return strikeCnt;
    }

    public boolean isNothing() { return nothing; }

    public void add(BallResult result) {
        if (result.isBall()) ++ballCnt;
        if (result.isStrike()) ++strikeCnt;
        if (result.isNothing() ) nothing = true;
    }

    public boolean isGameOver() {
        return strikeCnt == GAME_OVER;
    }
}
