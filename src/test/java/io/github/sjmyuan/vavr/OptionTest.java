package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Either;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;;

public class OptionTest {


    @Test
    public void shouldConvertNullToNone() {
        Option<Integer> intValue = Option.of(null);
        assertThat(intValue).isEqualTo(Option.none());
    }

    @Test
    public void shouldConvertValueToSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldConvertOptionalToSomeIfPresent() {
        Optional<Integer> optionalInt = Optional.of(1);
        Option<Integer> intValue = Option.ofOptional(optionalInt);
        assertThat(intValue).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldConvertOptionalToSomeIfNotPresent() {
        Optional<Integer> optionalInt = Optional.ofNullable(null);
        Option<Integer> intValue = Option.ofOptional(optionalInt);
        assertThat(intValue).isEqualTo(Option.none());
    }


    @Test
    public void canBeConstructedByCondition() {
        Option<Integer> trueValue = Option.when(true, () -> 1);
        assertThat(trueValue).isEqualTo(Option.some(1));

        Option<Integer> trueValue2 = Option.when(true, 1);
        assertThat(trueValue2).isEqualTo(Option.some(1));

        Option<Integer> falseValue = Option.when(false, () -> 1);
        assertThat(falseValue).isEqualTo(Option.none());

        Option<Integer> falseValue2 = Option.when(false, 1);
        assertThat(falseValue2).isEqualTo(Option.none());
    }

    @Test
    public void canDoFilterForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.filter(x -> x > 2)).isEqualTo(Option.none());
        assertThat(intValue.filter(x -> x > 0)).isEqualTo(Option.some(1));
    }

    @Test
    public void canDoFilterForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThat(intValue.filter(x -> x > 2)).isEqualTo(Option.none());
        assertThat(intValue.filter(x -> x <= 0)).isEqualTo(Option.none());
    }

    @Test
    public void canDoMapForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.map(x -> x + 1)).isEqualTo(Option.some(2));
        assertThat(intValue.map(x -> x.toString())).isEqualTo(Option.some("1"));
    }

    @Test
    public void canDoMapForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThat(intValue.map(x -> x + 1)).isEqualTo(Option.none());
        assertThat(intValue.map(x -> x.toString())).isEqualTo(Option.none());
    }

    @Test
    public void canDoFlatMapForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.flatMap(x -> Option.some(x + 1))).isEqualTo(Option.some(2));
        assertThat(intValue.flatMap(x -> Option.none())).isEqualTo(Option.none());
        assertThat(intValue.flatMap(x -> Option.some(x.toString()))).isEqualTo(Option.some("1"));
    }

    @Test
    public void canDoFlatMapForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThat(intValue.flatMap(x -> Option.some(x + 1))).isEqualTo(Option.none());
        assertThat(intValue.flatMap(x -> Option.none())).isEqualTo(Option.none());
        assertThat(intValue.flatMap(x -> Option.some(x.toString()))).isEqualTo(Option.none());
    }

    @Test
    public void canDoPeekForSome() {
        Option<Integer> intValue = Option.of(1);
        List<Integer> intList = new ArrayList<Integer>();
        intValue.peek(x -> {
            intList.add(x);
        });
        assertThat(intList.size()).isEqualTo(1);
        assertThat(intList.get(0)).isEqualTo(1);
    }

    @Test
    public void canDoPeekForNone() {
        Option<Integer> intValue = Option.of(null);
        List<Integer> intList = new ArrayList<Integer>();
        intValue.peek(x -> {
            intList.add(x);
        });
        assertThat(intList.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnWrappedValueForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.get()).isEqualTo(1);
    }

    @Test
    public void shouldThrowErrorForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThatThrownBy(() -> intValue.get()).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }

    @Test
    public void canReturnDefaultValueForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThat(intValue.getOrElse(2)).isEqualTo(2);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(3);
    }

    @Test
    public void canIgnoreDefaultValueForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.getOrElse(2)).isEqualTo(1);
        assertThat(intValue.getOrElse(() -> 3)).isEqualTo(1);
    }

    @Test
    public void canThrowErrorForNone() {
        Option<Integer> intValue = Option.of(null);
        assertThatThrownBy(() -> intValue.getOrElseThrow(() -> new Error("empty value")))
                .isInstanceOf(Error.class).hasMessageContaining("empty value");
    }

    @Test
    public void canIgnoreThrowingErrorForSome() {
        Option<Integer> intValue = Option.of(1);
        assertThat(intValue.getOrElseThrow(() -> new Error("empty value"))).isEqualTo(1);
    }

    @Test
    public void canDoDifferentOperationForSomeAndNone() {
        assertThat(Option.of(1).fold(() -> "none", x -> x.toString())).isEqualTo("1");
        assertThat(Option.of(null).fold(() -> "none", x -> x.toString())).isEqualTo("none");
    }

    @Test
    public void canDoFoldLeft() {
        assertThat(Option.of(1).fold(() -> "none", x -> x.toString())).isEqualTo("1");
        assertThat(Option.of(null).fold(() -> "none", x -> x.toString())).isEqualTo("none");
    }

    @Test
    public void canBeChangedToOtherOptionForNone() {
        assertThat(Option.of(1).orElse(() -> Option.of(2))).isEqualTo(Option.of(1));
        assertThat(Option.of(1).orElse(Option.of(2))).isEqualTo(Option.of(1));
        assertThat(Option.of(null).orElse(() -> Option.of(2))).isEqualTo(Option.of(2));
        assertThat(Option.of(null).orElse(Option.of(2))).isEqualTo(Option.of(2));
    }

    @Test
    public void canBeChangedToEither() {
        assertThat(Option.of(1).toEither("Error")).isEqualTo(Either.right(1));
        assertThat(Option.of(null).toEither("Error")).isEqualTo(Either.left("Error"));
    }

    @Test
    public void supportToCheckType() {
        assertThat(Option.some(1).isDefined()).isTrue();
        assertThat(Option.some(1).isEmpty()).isFalse();
        assertThat(Option.none().isDefined()).isFalse();
        assertThat(Option.none().isEmpty()).isTrue();
    }
}
