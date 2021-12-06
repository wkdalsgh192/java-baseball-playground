package baseball;

enum BallResult {
    STRIKE,
    BALL,
    NOTHING;

    public boolean isNotNothing() {
        return this != NOTHING;
    }

    public boolean isBall() { return this == BALL; }

    public boolean isStrike() { return this == STRIKE; }
}
