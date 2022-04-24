package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static io.vavr.API.*;
import static io.vavr.Patterns.*;

public class PatternTest {

    @Test
    public void canMatchOption() {

        // Integer result = Match(Option.some(1)).of(Case($Some($()), 1), Case($None(), 2));

        // assertThat(result).isEqualTo(1);

    }

    @Test
    public void canMatchLikeSwitch() {

        // Integer result = Match(Option.some(1)).of(Case($(1), 1), Case($(2), 2));

        // assertThat(result).isEqualTo(1);

    }

}
