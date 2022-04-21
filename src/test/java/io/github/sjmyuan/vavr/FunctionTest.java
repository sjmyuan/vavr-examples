package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionTest {

    @Test
    public void canBeComposed() {
        Function1<String, String> add1 = x -> x + "1";
        Function1<String, String> add2 = x -> x + "2";

        assertThat(add1.compose(add2).apply("0")).isEqualTo("021");
        assertThat(add1.andThen(add2).apply("0")).isEqualTo("012");
    }

    @Test
    public void canBeCurried() {
        Function2<String, String, String> add = (x, y) -> x + y;
        assertThat(add.curried().apply("0").apply("1")).isEqualTo("01");
    }

    @Test
    public void canBePartialApplied() {
        Function2<String, String, String> add = (x, y) -> x + y;
        assertThat(add.apply("0").apply("1")).isEqualTo("01");
    }

    @Test
    public void canLiftThePartialFunction() {
        Function2<Integer, Integer, Integer> division = (x, y) -> x / y;
        assertThat(Function2.lift(division).apply(1, 0)).isEqualTo(Option.none());
    }

}
