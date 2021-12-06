package baseball;

class ScoreBoard {
    private int ball;
    private int strike;
    private boolean isNothing;

    public int getBall() { return ball; }

    public int getStrike() {
        return strike;
    }

    public void add(BallResult result) {
        if (result.isBall()) ++ball;
        if (result.isStrike()) ++strike;
        if (result.isNothing() ) isNothing = true;
    }
}
