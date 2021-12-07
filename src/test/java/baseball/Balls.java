package baseball;

import java.util.List;
import java.util.stream.Collectors;

import static baseball.BallNumber.FIRST;
import static baseball.BallResult.*;

class Balls {

    private final static int MAGIC_NUMBER = 3;
    private List<Ball> ballList;

    public Balls(List<Integer> input) {
        if (input.size() != MAGIC_NUMBER) throw new IllegalArgumentException("3자리 수만 입력이 가능합니다.");

        Atomic<BallNumber> atomic = new Atomic();
        atomic.set(FIRST);
        ballList = input.stream().map((number) -> new Ball(atomic.getAndIncrement(), number)).collect(Collectors.toList());
    }

    public ScoreBoard play(Balls target) {
        ScoreBoard scoreBoard = new ScoreBoard();
        for (Ball ball : target.ballList) {
            scoreBoard.add(getResultBy(ball));
        }
        return scoreBoard;
    }

    private BallResult getResultBy(Ball other) {
        for (Ball ball : ballList) {
            if (ball.equals(other)) return STRIKE;
            if (ball.hasSameValueWith(other)) return BALL;
        }
        return NOTHING;
    }
}
