package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
// import static io.vavr.API.*;

public class TryTest {


    @Test
    public void canBeConstructedFromAFunctionWithoutException() {
        Try<Integer> intValue = Try.of(() -> 1);
        assertThat(intValue.isSuccess()).isTrue();
        assertThat(intValue.isFailure()).isFalse();
        assertThat(intValue.get()).isEqualTo(1);
        assertThatThrownBy(() -> {
            intValue.getCause();
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void canBeConstructedFromAValue() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.isSuccess()).isTrue();
        assertThat(intValue.isFailure()).isFalse();
        assertThat(intValue.get()).isEqualTo(1);
        assertThatThrownBy(() -> {
            intValue.getCause();
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void canBeConstructedFromAFunctionWithException() {
        Try<Integer> intValue = Try.of(() -> Integer.parseInt("a"));
        assertThat(intValue.isSuccess()).isFalse();
        assertThat(intValue.isFailure()).isTrue();
        assertThat(intValue.getCause()).isInstanceOf(Exception.class);
        assertThatThrownBy(() -> {
            intValue.get();
        }).isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void canBeConstructedFromAnException() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThat(intValue.isSuccess()).isFalse();
        assertThat(intValue.isFailure()).isTrue();
        assertThat(intValue.getCause()).isInstanceOf(Exception.class);
        assertThatThrownBy(() -> {
            intValue.get();
        }).isInstanceOf(Exception.class);
    }

    @Test
    public void canDoFilterForSuccess() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.filter(x -> x > 2).getCause())
                .isInstanceOf(NoSuchElementException.class);
        assertThat(intValue.filter(x -> x > 0).get()).isEqualTo(1);
    }

    @Test
    public void canDoFilterForFailure() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThat(intValue.filter(x -> x > 2).getCause()).isInstanceOf(Exception.class);
        assertThat(intValue.filter(x -> x <= 0).getCause()).isInstanceOf(Exception.class);
    }

    @Test
    public void canDoFilterForFailureWithCustomError() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThat(intValue.filter(x -> x > 2, (x) -> new Exception("Bad")).getCause())
                .hasMessageContaining("Error");
        assertThat(intValue.filter(x -> x <= 0, (x) -> new Exception("Bad")).getCause())
                .hasMessageContaining("Error");
    }

    @Test
    public void canDoFilterForSuccessWithCustomError() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.filter(x -> x > 2, (x) -> new Exception("Bad")).getCause())
                .hasMessageContaining("Bad");
        assertThat(intValue.filter(x -> x > 0, (x) -> new Exception("Bad")).get()).isEqualTo(1);
    }

    @Test
    public void canDoMapForSuccess() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.map(x -> x + 1)).isEqualTo(Try.success(2));
        assertThat(intValue.map(x -> x.toString())).isEqualTo(Try.success("1"));
    }

    @Test
    public void canDoMapForFailure() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        // assertThat(intValue.mapFailure(Case($(), new Exception("Bad"))).getCause())
        //         .hasMessageContaining("Bad");
        assertThat(intValue.map(x -> x + 1)).isEqualTo(intValue);
    }

    @Test
    public void canDoFlatMapForSuccess() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.flatMap(x -> Try.success(x + 1))).isEqualTo(Try.success(2));
        assertThat(intValue.flatMap(x -> Try.failure(new Exception("Error"))).getCause())
                .hasMessageContaining("Error");
        assertThat(intValue.flatMap(x -> Try.success(x.toString()))).isEqualTo(Try.success("1"));
    }

    @Test
    public void canDoPeekForSuccess() {
        Try<Integer> intValue = Try.success(1);
        List<Integer> intList = new ArrayList<Integer>();
        intValue.peek(x -> {
            intList.add(x);
        });
        assertThat(intList.size()).isEqualTo(1);
        assertThat(intList.get(0)).isEqualTo(1);

        Try<Integer> intValue2 = Try.failure(new Exception("Error"));
        List<Integer> intList2 = new ArrayList<Integer>();
        intValue2.peek(x -> {
            intList2.add(x);
        });
        assertThat(intList2.size()).isEqualTo(0);
    }

    @Test
    public void canReturnDefaultValueForFailure() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThat(intValue.getOrElse(2)).isEqualTo(2);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(3);
    }

    @Test
    public void canReturnNullForFailure() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThat(intValue.getOrNull()).isNull();

        Try<Integer> intValue2 = Try.success(1);
        assertThat(intValue2.getOrNull()).isEqualTo(1);
    }

    @Test
    public void canIgnoreDefaultValueFoSuccess() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.getOrElse(2)).isEqualTo(1);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(1);
    }

    @Test
    public void canThrowErrorForLeft() {
        Try<Integer> intValue = Try.failure(new Exception("Error"));
        assertThatThrownBy(() -> intValue.getOrElseThrow(() -> new Error("empty value")))
                .isInstanceOf(Error.class).hasMessageContaining("empty value");
    }

    @Test
    public void canIgnoreThrowingErrorForRight() {
        Try<Integer> intValue = Try.success(1);
        assertThat(intValue.getOrElseThrow(() -> new Error("empty value"))).isEqualTo(1);
    }

    @Test
    public void canDoDifferentOperationForSuccessAndFailure() {
        assertThat(Try.success(1).<String>fold((e) -> "failure", x -> x.toString())).isEqualTo("1");
        assertThat(
                Try.failure(new Exception("Error")).<String>fold((e) -> "failure", x -> x.toString()))
                        .isEqualTo("failure");
    }

    @Test
    public void canBeChangedToOtherTryForFailure() {
        assertThat(Try.success(1).orElse(() -> Try.success(2))).isEqualTo(Try.success(1));
        assertThat(Try.success(1).orElse(Try.success(2))).isEqualTo(Try.success(1));
        assertThat(Try.failure(new Exception("Error")).orElse(() -> Try.success(2)))
                .isEqualTo(Try.success(2));
        assertThat(Try.failure(new Exception("Error")).orElse(Try.success(2)))
                .isEqualTo(Try.success(2));
    }

    @Test
    public void canBeChangedToOption() {
        assertThat(Try.success(1).toOption()).isEqualTo(Option.some(1));
        assertThat(Try.failure(new Exception("Error")).toOption()).isEqualTo(Option.none());
    }

    @Test
    public void canBeChangedToEither() {
        assertThat(Try.success(1).toEither()).isEqualTo(Either.right(1));
        assertThat(Try.failure(new Exception("Error")).toEither().getLeft())
                .hasMessageContaining("Error");
    }
}
