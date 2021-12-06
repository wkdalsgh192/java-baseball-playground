package baseball;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static baseball.BallNumber.*;

public class BaseballTest {

    Balls balls = new Balls(Arrays.asList(1,2,3));

    @Test
    @DisplayName("정답을 맞춘다.")
    void correct() {
        Balls answers = new Balls(Arrays.asList(1,2,3));
        // Assertions.assertThat(balls.isCorrect(answers)).isTrue();

        answers = new Balls(Arrays.asList(1,4,2));
        ///Assertions.assertThat(balls.isCorrect(answers)).isFalse();
    }

    @Test
    @DisplayName("낫싱 테스트")
    void nothing() {
        Balls answers = new Balls(Arrays.asList(4,5,6));
        Assertions.assertThat(balls.isNothing(answers)).isTrue();
    }

    @Test
    @DisplayName("3볼 테스트")
    void threeball() {
        Balls answers = new Balls(Arrays.asList(2,3,1));
        // Assertions.assertThat(balls.isAllBall(answers)).isTrue();
    }

    @Test
    @DisplayName("2볼 1스트라이크 테스트")
    void twoBallOneStrike() {
        Balls answers = new Balls(Arrays.asList(1,3,2));
        ScoreBoard scoreBoard = answers.play(balls);

        Assertions.assertThat(scoreBoard.getBall()).isEqualTo(2);
        Assertions.assertThat(scoreBoard.getStrike()).isEqualTo(1);
    }


}
