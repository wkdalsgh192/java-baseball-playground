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

    public boolean isCorrect(Balls other) {
        long strikeCnt = ballList.stream().filter(ball -> ball.compareTo(other.findBy(ball.getPos())) == BallResult.STRIKE).count();
        return strikeCnt == MAGIC_NUMBER;
    }

    public boolean isNothing(Balls other) {
        long ballCnt = other.ballList.stream().filter(ball -> !values().contains(ball)).count();
        return ballCnt == MAGIC_NUMBER;
    }


    private List<Integer> values() {
        return ballList.stream().map(ball -> ball.getValue()).collect(Collectors.toList());
    }

    private Ball findBy(BallNumber number) {
        return ballList.stream().filter(ball -> ball.getPos() == number).findAny().orElseThrow(IllegalArgumentException::new);
    }

    private Ball findBy(int value) {
        return ballList.stream().filter(ball -> ball.getValue() == value).findAny().get();
    }

    public boolean isAllBall(Balls other) {
        long ballCnt = ballList.stream().filter(ball -> ball.compareTo(other.findBy(ball.getValue())) == BallResult.BALL).count();
        return ballCnt == MAGIC_NUMBER;
    }

    public ScoreBoard play(Balls answer) {
        ScoreBoard scoreBoard = new ScoreBoard();
        return scoreBoard;
    }

}
