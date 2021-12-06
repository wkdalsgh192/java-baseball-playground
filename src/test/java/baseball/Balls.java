package baseball;

import java.util.List;
import java.util.stream.Collectors;

import static baseball.BallNumber.FIRST;

class Balls {

    private final static int MAGIC_NUMBER = 3;
    private List<Ball> ballList;

    public Balls(List<Integer> input) {
        if (input.size() != MAGIC_NUMBER) throw new IllegalArgumentException("3자리 수만 입력이 가능합니다.");

        Atomic<BallNumber> atomic = new Atomic();
        atomic.set(FIRST);
        ballList = input.stream().map((number) -> new Ball(atomic.getAndIncrement(), number)).collect(Collectors.toList());
    }

    public boolean isNothing(Balls other) {
        long ballCnt = other.ballList.stream().filter(ball -> !values().contains(ball)).count();
        return ballCnt == MAGIC_NUMBER;
    }
    private List<Integer> values() {
        return ballList.stream().map(ball -> ball.getValue()).collect(Collectors.toList());
    }

    public ScoreBoard play(Balls target) {
        ScoreBoard scoreBoard = new ScoreBoard();
        for (Ball ball : target.ballList) {
            Ball answerBall = findBy(ball.getPos());
            if (answerBall != null && answerBall.getValue() == ball.getValue()) {
                scoreBoard.add(BallResult.STRIKE);
            } else {
                answerBall = findBy(ball.getValue());
                if (answerBall != null) scoreBoard.add(BallResult.BALL);
                else scoreBoard.add(BallResult.NOTHING);
            }
        }
        return scoreBoard;
    }

    private Ball findBy(int value) {
        for (Ball ball : ballList) {
            if (ball.getValue() == value) return ball;
        }
        return null;
    }

    private Ball findBy(BallNumber number) {
        for (Ball ball : ballList) {
            if (ball.getPos() == number) return ball;
        }
        return null;
    }

}
