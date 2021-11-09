package study;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetCollectionTest {
    private Set<Integer> numbers;

    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
    }

    @Test
    @DisplayName("Set의 크기를 확인한다")
    void size() {
        assertThat(numbers.size()).isEqualTo(3);
    }

    @DisplayName("주어진 파라미터값이 Set에 존재하는 지 확인한다.")
    @ParameterizedTest
    @ValueSource(ints={1,2,3})
    void contains(int i) {
        assertTrue(numbers.contains(i));
    }

    @ParameterizedTest
    @CsvSource(value = {"1,true","2,true","3,true","4,false","5,false"}, delimiter = ',')
    void containsTrueOrFalse(String param, String expected) {
        Integer actual = Integer.valueOf(param);
        assertThat(numbers.contains(actual)).isEqualTo(Boolean.valueOf(expected));
    }
}
