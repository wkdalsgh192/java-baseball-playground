package baseball;

enum BallNumber {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private int num;

    BallNumber(int num) {
        this.num = num;
    }
}
