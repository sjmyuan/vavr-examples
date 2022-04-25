package io.github.sjmyuan.vavr;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SideEffectTest {
    public void append(List<Integer> intList1, List<Integer> intList2) {
        intList1.addAll(intList2);
        return;
    }

    public Integer getHead(java.util.List<Integer> intList) {
        if (intList.size() == 0) {
            return null;
        } else {
            return intList.get(0);
        }
    }

    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new Error("The denominator can not be 0");
        } else {
            return x / y;
        }
    }

    public Integer add(String x, String y) {
        Integer xInt = Integer.valueOf(x);
        Integer yInt = Integer.valueOf(y);
        return xInt + yInt;
    }

    @Test
    public void modifyParameterIsSideEffect() {

        List<Integer> intList1 = new LinkedList<Integer>();
        intList1.add(1);
        intList1.add(2);

        List<Integer> intList2 = new LinkedList<Integer>();
        intList2.add(3);
        intList2.add(4);

        this.append(intList1, intList2);

        assertThat(intList1.size()).isEqualTo(4);
        assertThat(intList2.size()).isEqualTo(2);
    }

    @Test
    public void consumerNeedToCheckNull() {
        Integer head = getHead(new LinkedList<Integer>());

        String result = head == null ? "No Element" : head.toString();

        assertThat(result).isEqualTo("No Element");
    }

    @Test
    public void stringToIntegerMayThrowError() {
        assertThatThrownBy(() -> {
            add("1", "a");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
