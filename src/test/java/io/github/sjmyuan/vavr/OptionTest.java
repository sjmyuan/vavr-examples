package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.*;

public class OptionTest {


    @Test
    void convertNullToNone() {
        Option<Integer> intValue = Option.of(null);
    }
}
