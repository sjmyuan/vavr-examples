package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.collection.List;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;

public class ListAndOptionTest {

    Integer sum(List<Integer> list) {
        return list.fold(0, (acc, ele) -> acc + ele);
    }

    @Test
    public void getFirstElement() {
        List<Integer> list = List.of(1, 2, 3);
        assertThat(list.get(0)).isEqualTo(1);
    }

    @Test
    public void getFirstElementWithOption() {
        List<Integer> list = List.of(1, 2, 3);
        assertThat(list.headOption()).isEqualTo(Option.some(1));
    }

    @Test
    public void optionToList() {
        Option<Integer> option = Option.some(1);

        List<Integer> convertedList = option.isDefined() ? List.of(option.get()) : List.empty(); // List(1)

        Integer sumValue = sum(convertedList);

        assertThat(sumValue).isEqualTo(1);
    }

    @Test
    public void optionToListWithOfAll() {
        Option<Integer> option = Option.some(1);

        Integer sumValue = sum(List.ofAll(option));

        assertThat(sumValue).isEqualTo(1);
    }
}
