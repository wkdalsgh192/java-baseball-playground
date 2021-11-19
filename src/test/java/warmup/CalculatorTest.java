package warmup;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.lang.IllegalArgumentException;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CalculatorTest {

    @ParameterizedTest
    @DisplayName("숫자를 split()로 쪼갠다")
    @MethodSource("argumentsStream")
    void split(String expr, int answer) {
        String[] values = expr.split(" ");
        Assertions.assertThat(values.length).isEqualTo(Integer.valueOf(answer));
    }

    @Nested
    @DisplayName("가능한 경우의 예외 케이스를 검증한다.")
    class ExceptionHandling {

        @ParameterizedTest
        @DisplayName("숫자로 변환할 수 있는 값이 아니면 예외를 발생시킨다.")
        @ValueSource(strings = {"a * 3"})
        void invokeExceptionWhenImproperValueIncluded(String expr) {
            Assertions.assertThatThrownBy(() -> {
                String[] values = expr.split(" ");
                for (int i=0;i<values.length-2;i+=2) {
                    calc(values[i], values[i+1],values[i+2]);

                }
            }).isInstanceOf(NumberFormatException.class).hasMessageContaining("숫자 형식의 입력값이 아닙니다.");
        }

        @ParameterizedTest
        @DisplayName("알맞지 않는 연산자가 들어오면 예외를 발생시킨다.")
        @ValueSource(strings = {"2 [ 3"})
        void invokeExceptionWhenImproperOperatorIncluded(String expr) {
            Assertions.assertThatThrownBy(() -> {
                String[] values = expr.split(" ");
                for (int i=0;i<values.length-2;i+=2) {
                    calc(values[i], values[i+1],values[i+2]);
                }
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("허용되지 않은 연산자를 사용하였습니다.");
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    @DisplayName("각각의 연산자에 대한 기본 테스트")
    void doUnitCalculation(String expr, Integer expt) {
        // given
        String[] values = expr.split(" ");
        Assertions.assertThat(calc(values[0], values[1],values[2])).isEqualTo(expt);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream2")
    @DisplayName("사용자의 입력값에 따라 다르게 사칙연산 수행")
    void doCustomCalculation(String input, Integer answer) {
        Scanner scanner = generateUserInput(input);
        String expr = scanner.nextLine();
        String[] values = expr.split(" ");
        Integer res = 0;

        for (int i = 0; i < values.length; i+=2) {
            if (i == values.length -1) {
                res = Integer.valueOf(values[i]);
                break;
            }

            res = calc(values[i], values[i+1],values[i+2]);
            System.out.println(res);
            values[i+2] = String.valueOf(res);
        }

        Assertions.assertThat(res).isEqualTo(answer);
    }

    private Scanner generateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new Scanner(System.in);
    }

    private int calc(String arg1, String operator, String arg2) {
        System.out.println(arg1 + " " + operator + " " + arg2);

        if (arg1.matches(".*[a-zA-Z]+.*") || arg2.matches(".*[a-zA-Z]+.*")) throw new NumberFormatException("숫자 형식의 입력값이 아닙니다.");
        if (!"+-*/".contains(operator)) throw new IllegalArgumentException("허용되지 않은 연산자를 사용하였습니다.");

        Integer a = Integer.valueOf(arg1);
        Integer b = Integer.valueOf(arg2);

        if (operator.equals("+")) { return a+b; }
        if (operator.equals("-")) { return a-b; }
        if (operator.equals("*")) { return a*b; }
        if (operator.equals("/")) { return a/b; }

        return 0;
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments("2 + 3", 5),
                arguments("2 - 3", -1),
                arguments("2 * 4", 8),
                arguments("5 / 2", 2)
        );
    }

    private static Stream<Arguments> argumentsStream2() {
        return Stream.of(
                arguments("2 + 3 * 4 / 2", 10)
        );
    }



}
