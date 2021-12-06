package baseball;

import static baseball.BallNumber.*;

class Atomic<T> {
    private T data;

    public void set(T value) {
        data = value;
    }

    public BallNumber getAndIncrement() {
        BallNumber curr = (BallNumber) data;

        if (data == FIRST) data = (T) SECOND;
        else if (data == SECOND) data = (T) THIRD;
        else if (data == THIRD) data = (T) FIRST;

        return curr;
    }
}
