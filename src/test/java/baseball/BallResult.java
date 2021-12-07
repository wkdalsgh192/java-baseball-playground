package baseball;

enum BallResult {
    STRIKE,
    BALL,
    NOTHING;

    public boolean isBall() { return this == BALL; }

    public boolean isStrike() { return this == STRIKE; }

    public boolean isNothing() { return this == NOTHING; }
}
