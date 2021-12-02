package baseball;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static baseball.BaseballTest.BallNumber.*;

public class BaseballTest {

    @Test
    @DisplayName("스트라이크 테스트")
    void strike() {
        Ball ball = new Ball(FIRST,1);
        Ball answer = new Ball(FIRST,1);

        Assertions.assertThat(ball.compareTo(answer)).isEqualTo(BallResult.STRIKE);
    }

    @Test
    @DisplayName("볼 테스트")
    void ball() {
        Ball ball = new Ball(BallNumber.SECOND,1);
        Ball answer = new Ball(FIRST, 1);

        Assertions.assertThat(ball.compareTo(answer)).isEqualTo(BallResult.BALL);
    }

    @Test
    @DisplayName("정답을 맞춘다.")
    void correct() {
        Balls balls = new Balls(Arrays.asList(1,2,3));
        Balls answers = new Balls(Arrays.asList(1,2,3));

        Assertions.assertThat(balls.isCorrect(answers)).isTrue();

        answers = new Balls(Arrays.asList(1,4,2));
        Assertions.assertThat(balls.isCorrect(answers)).isFalse();
    }

    @Test
    @DisplayName("낫싱 테스트")
    void nothing() {
        Balls balls = new Balls(Arrays.asList(1,2,3));
        Balls answers = new Balls(Arrays.asList(4,5,6));

        Assertions.assertThat(balls.isNothing(answers)).isTrue();
    }

    @Test
    @DisplayName("3볼 테스트")
    void threeball() {
        Balls balls = new Balls(Arrays.asList(1,2,3));
        Balls answers = new Balls(Arrays.asList(2,3,1));

        Assertions.assertThat(balls.isAllBall(answers)).isTrue();
    }


    private class Ball {

        private BallNumber pos;
        private int value;

        public Ball(BallNumber pos, int value) {
            this.pos = pos;
            this.value = value;
        }

        public BallNumber getPos() { return pos; }

        public int getValue() { return value; }

        public BallResult compareTo(Ball other) {
            if (this.pos == other.getPos() && this.value == other.getValue()) return BallResult.STRIKE;
            if (this.pos != other.getPos() && this.value == other.getValue()) return BallResult.BALL;
            return BallResult.NOTHING;
        }
    }

    private class Balls {

        private final static int MAGIC_NUMBER = 3;
        private List<Ball> list;

        public Balls(List<Integer> input) {
            if (input.size() != MAGIC_NUMBER) throw new IllegalArgumentException("3자리 수만 입력이 가능합니다.");

            Atomic<BallNumber> atomic = new Atomic();
            atomic.set(FIRST);
            list = input.stream().map((number) -> new Ball(atomic.getAndIncrement(), number)).collect(Collectors.toList());
        }

        public boolean isCorrect(Balls other) {
            long strikeCnt = list.stream().filter(ball -> ball.compareTo(other.findBy(ball.pos)) == BallResult.STRIKE).count();
            return strikeCnt == MAGIC_NUMBER;
        }

        public boolean isNothing(Balls other) {
            long ballCnt = other.list.stream().filter(ball -> !values().contains(ball)).count();
            return ballCnt == MAGIC_NUMBER;
        }

        private long count(Balls other, BallResult expected) {
            return list.stream().filter(ball -> ball.compareTo(other.findBy(ball.pos)) == expected).count();
        }

        private List<Integer> values() {
            return list.stream().map(ball -> ball.value).collect(Collectors.toList());
        }

        private Ball findBy(BallNumber number) {
            return list.stream().filter(ball -> ball.pos == number).findAny().orElseThrow(IllegalArgumentException::new);
        }

        private Ball findBy(int value) {
            return list.stream().filter(ball -> ball.value == value).findAny().get();
        }

        public boolean isAllBall(Balls other) {
            long ballCnt = list.stream().filter(ball -> ball.compareTo(other.findBy(ball.value)) == BallResult.BALL).count();
            return ballCnt == MAGIC_NUMBER;
        }
    }

    enum BallNumber {
        FIRST(1),
        SECOND(2),
        THIRD(3);

        private int num;

        BallNumber(int num) { this.num = num; }
    }

    private class Atomic<T> {
        private T data;

        public void set(T value) { data = value;}

        public BallNumber getAndIncrement() {
            BallNumber curr = (BallNumber) data;

            if (data == FIRST) data = (T) SECOND;
            else if (data == SECOND) data = (T) THIRD;
            else if (data == THIRD) data = (T) FIRST;

            return curr;
        }
    }

    private enum BallResult {
        STRIKE,
        BALL,
        NOTHING;
    }
}
