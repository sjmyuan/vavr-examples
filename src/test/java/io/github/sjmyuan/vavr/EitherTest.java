package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Either;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EitherTest {


    @Test
    public void canBeConstructedFromALeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.isLeft()).isTrue();
        assertThat(intValue.isRight()).isFalse();
        assertThat(intValue.getLeft()).isEqualTo("Error");
        assertThatThrownBy(() -> {
            intValue.get();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void canBeConstructedFromARight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.isLeft()).isFalse();
        assertThat(intValue.isRight()).isTrue();
        assertThat(intValue.get()).isEqualTo(1);
        assertThatThrownBy(() -> {
            intValue.getLeft();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void canDoFilterForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.filter(x -> x > 2)).isEqualTo(Option.none());
        assertThat(intValue.filter(x -> x > 0)).isEqualTo(Option.some(intValue));
    }

    @Test
    public void canDoFilterForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.filter(x -> x > 2)).isEqualTo(Option.some(intValue));
        assertThat(intValue.filter(x -> x <= 0)).isEqualTo(Option.some(intValue));
    }

    @Test
    public void canDoFilterForLeftWithCustomError() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.filterOrElse(x -> x > 2, (x) -> "Bad")).isEqualTo(intValue);
        assertThat(intValue.filterOrElse(x -> x <= 0, (x) -> "Bad")).isEqualTo(intValue);
    }

    @Test
    public void canDoFilterForRightWithCustomError() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.filterOrElse(x -> x > 2, (x) -> "Bad")).isEqualTo(Either.left("Bad"));
        assertThat(intValue.filterOrElse(x -> x > 0, (x) -> "Bad")).isEqualTo(intValue);
    }

    @Test
    public void canDoMapForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.map(x -> x + 1)).isEqualTo(Either.right(2));
        assertThat(intValue.map(x -> x.toString())).isEqualTo(Either.right("1"));
        assertThat(intValue.mapLeft(x -> x + " Left Map")).isEqualTo(intValue);
    }

    @Test
    public void canDoMapForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.mapLeft(x -> x + " Left Map")).isEqualTo(Either.left("Error Left Map"));
        assertThat(intValue.mapLeft(x -> 1)).isEqualTo(Either.left(1));
        assertThat(intValue.map(x -> x + 1)).isEqualTo(intValue);
    }

    @Test
    public void canDoFlatMapForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.flatMap(x -> Either.right(x + 1))).isEqualTo(Either.right(2));
        assertThat(intValue.flatMap(x -> Either.left("Error"))).isEqualTo(Either.left("Error"));
        assertThat(intValue.flatMap(x -> Either.right(x.toString()))).isEqualTo(Either.right("1"));
    }

    @Test
    public void canDoPeekForRight() {
        Either<String, Integer> intValue = Either.right(1);
        List<Integer> intList = new ArrayList<Integer>();
        intValue.peek(x -> {
            intList.add(x);
        });
        assertThat(intList.size()).isEqualTo(1);
        assertThat(intList.get(0)).isEqualTo(1);

        Either<String, Integer> intValue2 = Either.left("Error");
        List<Integer> intList2 = new ArrayList<Integer>();
        intValue2.peek(x -> {
            intList2.add(x);
        });
        assertThat(intList2.size()).isEqualTo(0);
    }

    @Test
    public void canDoPeekForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        List<String> stringList = new ArrayList<String>();
        intValue.peekLeft(x -> {
            stringList.add(x);
        });
        assertThat(stringList.size()).isEqualTo(1);
        assertThat(stringList.get(0)).isEqualTo("Error");

        Either<String, Integer> intValue2 = Either.right(1);
        List<String> stringList2 = new ArrayList<String>();
        intValue2.peekLeft(x -> {
            stringList2.add(x);
        });
        assertThat(stringList2.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnWrappedValueForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.get()).isEqualTo(1);

        Either<String, Integer> intValue2 = Either.left("Error");
        assertThatThrownBy(() -> intValue2.get()).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("get() on Left");
    }

    @Test
    public void shouldReturnWrappedValueForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.getLeft()).isEqualTo("Error");

        Either<String, Integer> intValue2 = Either.right(1);
        assertThatThrownBy(() -> intValue2.getLeft()).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("getLeft() on Right");
    }

    @Test
    public void canReturnDefaultValueForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.getOrElse(2)).isEqualTo(2);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(3);
    }

    @Test
    public void canReturnNullForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThat(intValue.getOrNull()).isNull();

        Either<String, Integer> intValue2 = Either.right(1);
        assertThat(intValue2.getOrNull()).isEqualTo(1);
    }

    @Test
    public void canIgnoreDefaultValueForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.getOrElse(2)).isEqualTo(1);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(1);
    }

    @Test
    public void canThrowErrorForLeft() {
        Either<String, Integer> intValue = Either.left("Error");
        assertThatThrownBy(() -> intValue.getOrElseThrow(() -> new Error("empty value")))
                .isInstanceOf(Error.class).hasMessageContaining("empty value");
    }

    @Test
    public void canIgnoreThrowingErrorForRight() {
        Either<String, Integer> intValue = Either.right(1);
        assertThat(intValue.getOrElseThrow(() -> new Error("empty value"))).isEqualTo(1);
    }

    @Test
    public void canDoDifferentOperationForLeftAndRight() {
        assertThat(Either.right(1).<String>fold((e) -> "left", x -> x.toString())).isEqualTo("1");
        assertThat(Either.left("Error").<String>fold((e) -> "left", x -> x.toString()))
                .isEqualTo("left");
    }

    @Test
    public void canBeChangedToOtherEitherForLeft() {
        assertThat(Either.right(1).orElse(() -> Either.right(2))).isEqualTo(Either.right(1));
        assertThat(Either.right(1).orElse(Either.right(2))).isEqualTo(Either.right(1));
        assertThat(Either.left("Error").orElse(() -> Either.right(2))).isEqualTo(Either.right(2));
        assertThat(Either.left("Error").orElse(Either.right(2))).isEqualTo(Either.right(2));
    }

    @Test
    public void canBeChangedToOption() {
        assertThat(Either.right(1).toOption()).isEqualTo(Option.some(1));
        assertThat(Either.left("Error").toOption()).isEqualTo(Option.none());
    }

    @Test
    public void supportEqual() {
        assertThat(Either.right(1)).isEqualTo(Either.right(1));
        assertThat(Either.left("Error")).isEqualTo(Either.left("Error"));
    }
}
