package baseball;

class Ball {

    private BallNumber pos;
    private int value;

    public Ball(BallNumber pos, int value) {
        if (value < 0 || value > 9) throw new IllegalArgumentException("0에서 9 사이의 값만 유효합니다.");
        this.pos = pos;
        this.value = value;
    }

    public boolean equals(Ball other) {
        if (pos == other.pos && value == other.value) return true;
        return false;
    }

    public boolean hasSameValueWith(Ball other) {
        if (value == other.value) return true;
        return false;
    }
}
