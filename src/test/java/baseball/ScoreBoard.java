package baseball;

class ScoreBoard {
    private int ball;
    private int strike;

    public int getBall() { return ball; }

    public int getStrike() {
        return strike;
    }

    public void add(BallResult result) {
        if (result.isBall()) ++ball;
        if (result.isStrike()) ++strike;
    }
}
