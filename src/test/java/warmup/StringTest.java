package warmup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringTest {

    String value = "1,2";

    @Test
    void replace() {
        String actual = "abc".replace("b", "d");
        assertThat(actual).isEqualTo("adc");
    }
    @Test
    @DisplayName("String의 split 메소드를 테스트해본다.")
    void split() {
        String[] actual = value.split(",");
        assertThat(actual).isEqualTo(new String[] {"1", "2"});

        actual = "1,2".split(",");
        assertThat(actual).containsExactly("1");
    }

    @Test
    @DisplayName("String의 substring 테스트")
    void substring() {
        value = "("+value+")";
        String actual = value.substring(1,value.length()-1);
        assertThat(actual).isEqualTo("1,2");
    }

    @Test
    @DisplayName("값이 주어졌을 때 charAt() 메소드를 활용해 특정 위치의 문자를 가져온다. 위치 값을 벗어나면 StringIndexOutOfBoundsException을 발생시킨다.")
    void charAt() {
        assertThatThrownBy(() -> {
            value = "abc";
            Integer[] ints = new Integer[] {1,4};
            Arrays.stream(ints).forEach((i) -> value.charAt(i));
        }).isInstanceOf(StringIndexOutOfBoundsException.class);
    }
}
