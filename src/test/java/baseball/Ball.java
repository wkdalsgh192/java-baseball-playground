package baseball;

class Ball {

    private BallNumber pos;
    private int value;

    public Ball(BallNumber pos, int value) {
        this.pos = pos;
        this.value = value;
    }

    public BallNumber getPos() {
        return pos;
    }

    public int getValue() {
        return value;
    }

    public BallResult compareTo(Ball other) {
        if (this.pos == other.getPos() && this.value == other.getValue()) return BallResult.STRIKE;
        if (this.pos != other.getPos() && this.value == other.getValue()) return BallResult.BALL;
        return BallResult.NOTHING;
    }
}
