package baseball;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        Assertions.assertThat(answers.isCorrect(balls)).isTrue();
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

            list = new ArrayList<>();
            Atomic<BallNumber> atomic = new Atomic();
            input.stream().map((number) -> new Ball(atomic.getAndIncrement(), number)).collect(Collectors.toList());
        }

        public boolean isCorrect(Balls other) {
            for (int i = 0; i < list.size(); i++) {
            }
            return true;
        }
    }

    enum BallNumber {
        FIRST(1),
        SECOND(2),
        THIRD(3);

        private int num;

        BallNumber(int num) { this.num = num; }

        public BallNumber increment() {
            if (this == FIRST) return SECOND;
            if (this == SECOND) return THIRD;
            return FIRST;
        }
    }

    private class Atomic<BallNumber> {
        private BallNumber data;

        public Atomic() {
            data = (BallNumber) FIRST;
        }

        public Atomic(BallNumber number) {
            data = number;
        }

        public BallNumber getAndIncrement() {
            BallNumber curr = data;
            /*data = data.increment();*/

            if (data == FIRST) data = (BallNumber) SECOND;
            if (data == SECOND) data = (BallNumber) THIRD;
            if (data == THIRD) data = (BallNumber) FIRST;

            return curr;
        }
    }

    private enum BallResult {
        STRIKE,
        BALL,
        NOTHING;
    }
}
