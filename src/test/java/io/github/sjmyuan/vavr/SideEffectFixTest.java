package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import static org.assertj.core.api.Assertions.assertThat;

public class SideEffectFixTest {
    public List<Integer> append(List<Integer> intList1, List<Integer> intList2) {
        return intList1.appendAll(intList2);
    }

    public Option<Integer> getHead(java.util.List<Integer> intList) {
        if (intList.size() == 0) {
            return Option.none();
        } else {
            return Option.some(intList.get(0));
        }
    }

    public Either<Error, Integer> divide(Integer x, Integer y) {
        if (y == 0) {
            return Either.left(new Error("The denominator can not be 0"));
        } else {
            return Either.right(x / y);
        }
    }

    public Try<Integer> add(String x, String y) {
        Try<Integer> xInt = Try.of(() -> Integer.valueOf(x));
        Try<Integer> yInt = Try.of(() -> Integer.valueOf(y));
        return xInt.flatMap(xv -> yInt.map(yv -> xv + yv));
    }

    @Test
    public void immutableListCanAvoidParameterModification() {

        List<Integer> intList1 = List.of(1, 2);

        List<Integer> intList2 = List.of(1, 2);

        List<Integer> result = this.append(intList1, intList2);

        assertThat(result.size()).isEqualTo(4);
        assertThat(intList1.size()).isEqualTo(2);
        assertThat(intList2.size()).isEqualTo(2);
    }

    @Test
    public void listCanCompareElement() {
        assertThat(List.of(1, 2)).isEqualTo(List.of(1, 2));
        assertThat(List.of(1, 2)).isNotEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void consumerAlwaysKnowOptionMaybeSomeOrNone() {
        Option<Integer> head = getHead(new java.util.LinkedList<Integer>());

        String result = head.fold(() -> "No Element", (x) -> x.toString());

        assertThat(result).isEqualTo("No Element");
    }

    @Test
    public void tryCanHandleError() {
        assertThat(add("1", "a").getCause()).isInstanceOf(IllegalArgumentException.class);
    }

}
