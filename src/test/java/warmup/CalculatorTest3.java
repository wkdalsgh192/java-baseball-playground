package warmup;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CalculatorTest3 {

    private final static int LIMIT = 1000;

    @Nested
    @DisplayName("모니터 작동 테스트")
    static class ViewTest {
        @ParameterizedTest
        @DisplayName("입력값이 제대로 들어오는 지 확인한다.")
        @MethodSource("argumentsStream")
        void accept_True_WhenInputSplitAndAddedToDeque(String input, int expected) {
            Deque<String> deque = new View().accept(input);
            Assertions.assertThat(deque.size()).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("띄어쓰기가 안 되어있으면 예외를 던진다")
        @ValueSource(strings = {"2*10", "2- 3 + 7"})
        void calcExpr_ThrowsException_WhenSpacingIsMissed(String input) {
            Assertions.assertThatThrownBy(() -> {
                new View().accept(input);
            }).isInstanceOf(IllegalStateException.class);
        }

        private static Stream<Arguments> argumentsStream() {
            return Stream.of(
                    arguments("2 - 3", 3),
                    arguments("2 + 3 * 4 / 2", 7),
                    arguments("41 - 1 * 10 / 8 + 50", 9)
            );
        }
    }

    @Nested
    @DisplayName("계산기 작동 시험")
    static class CalculationTest {
        @DisplayName("정상 작동하는 지에 관한 테스트")
        @ParameterizedTest(name = "[{index}] input: {0}, expected: {1}")
        @MethodSource("argumentsStream")
        void calcExpr_True_WhenCorrectInputIsGiven(String input, Integer expected) {
            Deque<String> deque = new View().accept(input);

            Number result = new Calculator().proceed(deque);

            Assertions.assertThat(result.value()).isEqualTo(expected);
        }

        @DisplayName("잘 못된 연산자가 들어오면 예외를 던진다.")
        @ParameterizedTest(name = "[{index}] input: {0}")
        @ValueSource(strings = {"2 & 3", "100 ^ 45", "81 ! 2 @ 10"})
        void calcExpr_ThrowsException_WhenOperatorIsNotValid(String input) {
            Deque<String> deque = new View().accept(input);

            Assertions.assertThatThrownBy(() -> {
                new Calculator().proceed(deque);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("허용되지 않는 연산자입니다.");
        }

        @DisplayName("0으로 나누려고 하면 예외를 던진다.")
        @ParameterizedTest(name = "[{index}] input: {0}")
        @ValueSource(strings = {"123 / 0"})
        void calcExpr_ThrowsException_WhenDiviedByZero(String input) {
            Deque<String> deque = new View().accept(input);

            Assertions.assertThatThrownBy(() -> {
                new Calculator().proceed(deque);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("0으로 나눠질 수 없습니다.");
        }

        private static Stream<Arguments> argumentsStream() {
            return Stream.of(
                    arguments("2 - 3", -1),
                    arguments("2 + 3 * 4 / 2", 10),
                    arguments("41 - 1 * 10 / 8 + 50", 100)
            );
        }
    }

    /**
     * 계산기에 입력값을 보내주기 위한 입력 장치.
     * 처리 과정: input(String) -> slicing(String Array) -> Queuing(Deque) -> SendingIn(Deque) -> SendingOut(Number)
    */
    static class View {

        private static final int MINIMUM_VALID_SIZE = 2;

        public View() {}

        public Deque accept(String input) {
            Scanner scanner = insert(input);
            return preprocess(scanner);
        }

        private Scanner insert(String input) {
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            return new Scanner(System.in);
        }

        private Deque preprocess(Scanner scanner) {
            String input = scanner.nextLine();
            List list = Arrays.stream(input.split(" ")).collect(Collectors.toList());
            Deque dq = new ArrayDeque();
            dq.addAll(list);

            if (dq.size() <= MINIMUM_VALID_SIZE || dq.size() % MINIMUM_VALID_SIZE == 0) throw new IllegalStateException("연산 불가: 띄어쓰기를 확인해주세요.");
            return dq;
        }

    }

    /**
     * 계산기는 연산 시점의 표현식을 받아서 계산하고 그 결과를 내보내는 연산장치.
     * 처리 과정: input(Deque) -> iterate -> calc -> return
     * */
    static class Calculator {

        private Operator operator;

        public enum Operator {
            PLUS("+", (Number first, Number second) -> {
                if (first.value()+second.value() > Integer.MAX_VALUE || first.value()+second.value() < Integer.MIN_VALUE) throw new IllegalArgumentException("연산값이 출력 허용 범위를 벗어납니다.");
                return new Number(first.value() + second.value());
            }),
            MINUS("-", (Number first, Number second) -> {
                if (first.value()-second.value() > Integer.MAX_VALUE || first.value()-second.value() < Integer.MIN_VALUE) throw new IllegalArgumentException("연산값이 출력 허용 범위를 벗어납니다.");
                return new Number(first.value() - second.value());
            }),
            MULTIPLY("*", (Number first, Number second) -> {
                if (first.value()*second.value() > Integer.MAX_VALUE || first.value()*second.value() < Integer.MIN_VALUE) throw new IllegalArgumentException("연산값이 출력 허용 범위를 벗어납니다.");
                return new Number(first.value() * second.value());
            }),
            DIVIDE("/", (Number first, Number second) -> {
                if (second.value() == 0) throw new IllegalArgumentException("0으로 나눠질 수 없습니다.");
                return new Number(first.value() / second.value());
            });

            private String symbol;
            private BinaryOperator<Number> function;

            Operator(String symbol, BinaryOperator<Number> function) {
                this.symbol = symbol;
                this.function = function;
            }

            public Number calculate(Number first, Number second) { return function.apply(first, second); }

            public static Operator find(String symbol) {
                Optional<Operator> opt = Arrays.stream(Operator.values()).filter((v) -> symbol.equals(v.symbol)).findAny();
                if (opt.isEmpty()) throw new IllegalArgumentException("허용되지 않는 연산자입니다.");
                return opt.get();
            }
        }

        // TODO: Iterate 처리를 계산기가 하는 게 맞나? 아니면 클래스를 새로 만들어야 할까?
        public Number proceed(Deque<String> deque) {
            int cnt = 0;
            Number result;
            String first,operator,second;
            while(!deque.isEmpty() && cnt < LIMIT) {
                if (deque.size() == 1) break;
                first = deque.pollFirst();
                operator = deque.pollFirst();
                second = deque.pollFirst();

                result = calc(Number.valueOf(first), Calculator.Operator.find(operator), Number.valueOf(second));
                deque.addFirst(result.toString());
                ++cnt;
            }

            if (deque.size() > 1) throw new IllegalStateException(String.format("최대 연산 가능 횟수({})를 초과하였습니다.", LIMIT));
            return Number.valueOf(deque.poll());
        }

        public Number calc(Number first, Operator operator, Number second) {
            switch (operator) {
                case PLUS:
                    return Operator.PLUS.calculate(first,second);
                case MINUS:
                    return Operator.MINUS.calculate(first,second);
                case MULTIPLY:
                    return Operator.MULTIPLY.calculate(first,second);
                default:
                    return Operator.DIVIDE.calculate(first,second);
            }
        }
    }

    final static class Number {

        private final int val;

        public Number(long val) {
            if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) throw new IllegalArgumentException("허용되지 않는 입력 범위 값입니다.");
            this.val = (int) val;
        }

        public String toString() {
            return String.valueOf(val);
        }

        public int value() {
            return this.val;
        }

        public static Number valueOf(String s) {
            return new Number(Long.parseLong(s));
        }
    }
}
