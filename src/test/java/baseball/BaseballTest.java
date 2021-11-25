package baseball;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class BaseballTest {

    @Test
    @DisplayName("플레이어 생성자가 만들어질 때마다 3자리 랜덤값이 만들어진다.")
    void cntNumDigit_EqualToLimit_WhenPlayerCreated() {
        Player player = Player.create();
        Assertions.assertThat(player.getAnswer().size()).isEqualTo(3);
    }

    final static class Player {

        private final int DIGIT_LIMIT = 3;
        private static Number answer = new Number();

        /**
         * 서로 다른 3자리 랜덤값을 생성한다.
         * */
        final static Player create() {
            Player player = new Player();
            Number number = player.answer;
            while (number.size() < player.DIGIT_LIMIT) {
                int i = new Random().nextInt(10);
                if (!number.contains(i)) number.add(i);
            }
            return player;
        }

        /**
         * number input은 무조건 3자리 수이다.
         *
         * */
         int check(String input) {
            int cnt = 0;
            for (int i = 0; i < input.length() ; i++) {
                if (answer.contains(input.charAt(i) - '0')) ++cnt;
            }
            return cnt;
        }

        Number getAnswer() {
             return answer;
        }
    }

    static final class Number {

        private List<Integer> list = new ArrayList<>();

        public boolean contains(int i) {
            return list.contains(i);
        }

        public int size() {
            return list.size();
        }

        public void add(int i) {
            list.add(i);
        }

    }
}
