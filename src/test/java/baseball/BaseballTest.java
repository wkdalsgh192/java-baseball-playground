package baseball;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class BaseballTest {

    Balls answers = new Balls(Arrays.asList(1,2,3));

    @Test
    @DisplayName("정답을 맞춘다.")
    void correct() {
        ScoreBoard scoreBoard = answers.play(new Balls(Arrays.asList(1,2,3)));
        Assertions.assertThat(scoreBoard.getStrikeCnt()).isEqualTo(3);

        scoreBoard = answers.play(new Balls(Arrays.asList(1,4,2)));
        Assertions.assertThat(scoreBoard.isGameOver()).isFalse();
    }

    @Test
    @DisplayName("낫싱 테스트")
    void nothing() {
        ScoreBoard scoreBoard = answers.play(new Balls(Arrays.asList(4,5,6)));
        Assertions.assertThat(scoreBoard.isNothing()).isTrue();
    }

    @Test
    @DisplayName("3볼 테스트")
    void threeball() {
        ScoreBoard scoreBoard = answers.play(new Balls(Arrays.asList(2,3,1)));

        Assertions.assertThat(scoreBoard.getBallCnt()).isEqualTo(3);
    }

    @Test
    @DisplayName("2볼 1스트라이크 테스트")
    void twoBallOneStrike() {
        ScoreBoard scoreBoard = answers.play(new Balls(Arrays.asList(1,3,2)));

        Assertions.assertThat(scoreBoard.getBallCnt()).isEqualTo(2);
        Assertions.assertThat(scoreBoard.getStrikeCnt()).isEqualTo(1);
    }
}
