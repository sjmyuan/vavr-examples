package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.collection.List;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    @Test
    public void canBeConstructedFromValues() {
        List<Integer> intList = List.of(1, 2, 3, 4, 5);
        assertThat(intList.size()).isEqualTo(5);

        List<Integer> intList2 = List.of(1);
        assertThat(intList2.size()).isEqualTo(1);
    }

    @Test
    public void canBeConstructedFromJavaList() {
        java.util.List<Integer> javaList = new java.util.LinkedList<>();
        javaList.add(1);
        javaList.add(2);
        javaList.add(3);

        List<Integer> intList = List.ofAll(javaList);
        assertThat(intList).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void canBeConstructedFromJavaStream() {
        java.util.stream.Stream<Integer> javaStream = java.util.stream.Stream.of(1, 2, 3);
        List<Integer> intList = List.ofAll(javaStream);
        assertThat(intList).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void canDoFilter() {
        List<Integer> intList = List.of(1, 2, 3);
        assertThat(intList.filter(x -> x < 2)).isEqualTo(List.of(1));
    }

    @Test
    public void canDoFold() {
        List<Integer> intList = List.of(1, 2, 3);
        assertThat(intList.foldLeft(0, (acc, ele) -> acc + ele)).isEqualTo(6);
    }

    @Test
    public void canDoMap() {
        List<Integer> intList = List.of(1, 2, 3);
        assertThat(intList.map(x -> x + 1)).isEqualTo(List.of(2, 3, 4));
    }

    @Test
    public void canDoFlatMap() {
        List<Integer> intList = List.of(1, 2, 3);
        assertThat(intList.flatMap(x -> List.of(x, x))).isEqualTo(List.of(1, 1, 2, 2, 3, 3));
    }

    @Test
    public void canDoSequence() {
        List<Option<Integer>> intList = List.of(Option.some(1), Option.some(2), Option.some(3));
        assertThat(Option.sequence(intList)).isEqualTo(Option.some(List.of(1, 2, 3)));

        List<Option<Integer>> intList2 = List.of(Option.some(1), Option.some(2), Option.none());
        assertThat(Option.sequence(intList2)).isEqualTo(Option.none());
    }

    @Test
    public void canDoTraverse() {
        List<Integer> intList = List.of(1, 2, 3);
        assertThat(Option.traverse(intList, x -> Option.some(x)))
                .isEqualTo(Option.some(List.of(1, 2, 3)));

        assertThat(Option.traverse(intList, x -> x > 1 ? Option.some(x) : Option.none()))
                .isEqualTo(Option.none());
    }
}
