package study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CalculatorTest2 {

    @ParameterizedTest
    @DisplayName("계산기 작동 테스트")
    @MethodSource("argumentsStream")
    void calcTest(String input, Integer expected) {
        System.out.println(input+" "+expected);
        Scanner scanner = generateUserInput(input);
        String expr = scanner.nextLine();

        Calculator calculator = new Calculator().create(expr);
        Assertions.assertThat(calculator.start()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("입력값이 올바르지 않을 때 예외를 발생시킨다.")
    @ValueSource(strings = {"2 / 5 *", "3 4"})
    void invokeExcpetionWhenInputIsNotValid(String input) {

        Assertions.assertThatThrownBy(() -> {
            Scanner scanner = generateUserInput(input);
            String expr = scanner.nextLine();

            Calculator calculator = new Calculator().create(expr);
            calculator.start();
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("입력값이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @DisplayName("입력값이 올바르지 않을 때 예외를 발생시킨다.")
    @ValueSource(strings = {"2 // 5 * 4", "3 [ 4 - 7", "8 = 87"})
    void invokeExcpetionWhenInputIsOutOfOperatorType(String input) {

        Assertions.assertThatThrownBy(() -> {
            Scanner scanner = generateUserInput(input);
            String expr = scanner.nextLine();

            Calculator calculator = new Calculator().create(expr);
            calculator.start();
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("올바른 연산자가 아닙니다.");
    }

    @ParameterizedTest
    @DisplayName("0으로 나누려고 할 때 예외를 발생시킨다.")
    @ValueSource(strings = {"2 * 5 / 0"})
    void invokeExcpetionWhenDividedByZero(String input) {

        Scanner scanner = generateUserInput(input);
        String expr = scanner.nextLine();

        Assertions.assertThatThrownBy(() -> {
            Calculator calculator = new Calculator().create(expr);
            calculator.start();
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("0으로 나눌 수 없습니다.");
    }

    private Scanner generateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new Scanner(System.in);
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments("2 + 3 * 4 / 2", 10),
                arguments("41 - 1 * 10 / 8 + 50", 100)
        );
    }

    class Calculator {

        private String expr;
        private int arg1;
        private int arg2;
        private Operator operator;

        public Calculator create(String expr) {
            Calculator calculator = new Calculator();
            calculator.expr = expr;
            return calculator;
        }

        public int start() {
            String[] values = split();
            int idx = 0;
            int curr = Integer.valueOf(values[idx]);

            for (int i=idx+1;i < values.length; i+=2) {
                insert(curr, Integer.valueOf(values[i+1]), values[i]);
                curr = calc();
            }
            return curr;
        }

        private String[] split() {
            String[] result =  expr.split(" ");
            // 제대로 분할되었는 지 확인 -> 짝수이면 exception 발생
            if (result.length < 3 || result.length % 2 == 0) throw new IllegalArgumentException("입력값이 올바르지 않습니다.");

            return result;
        }

        private void insert(int arg1, int arg2, String operator) {
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.operator = Operator.find(operator).orElseThrow(() -> {throw new IllegalArgumentException("올바른 연산자가 아닙니다."); });
        }

        private int calc() {
            switch(operator) {
                case PLUS:
                    return arg1+arg2;
                case MINUS:
                    return arg1-arg2;
                case MULTIPLY:
                    return arg1*arg2;
                default:
                    if (arg2 == 0) throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
                    return arg1/arg2;
            }
        }

    }

    enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/");

        Operator(String key) { this.key = key; }

        private final String key;
        public static Optional<Operator> find(String target) {
            return Arrays.stream(Operator.values()).filter((value) -> value.key.equals(target)).findAny();
        }
    }
}
