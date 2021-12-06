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

    public ScoreBoard play(Balls answer) {
        ScoreBoard scoreBoard = new ScoreBoard();
        // TODO 채워야하는 부분
        return scoreBoard;
    }

}
