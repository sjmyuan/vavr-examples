package io.github.sjmyuan.vavr;

import org.junit.Test;
import io.vavr.control.Either;
import io.vavr.control.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static io.vavr.API.*;
import static io.vavr.Patterns.*;

public class PatternTest {

    @Test
    public void canMatchOption() {

        Integer result =
                Match(Option.some(1)).of(Case($Some($(x -> x < 2)), x -> x + 10), Case($None(), 2));

        assertThat(result).isEqualTo(11);

    }

    @Test
    public void canMatchEither() {

        String result =
                Match(Either.right(1)).of(Case($Left($()), "Left"), Case($Right($()), x -> x.toString()));

        assertThat(result).isEqualTo("Right");

    }

    @Test
    public void canMatchLikeSwitch() {
        Integer intValue = 1;

        String result = Match(intValue).of(Case($(1), "1"), Case($(2), "2"), Case($(0), "-1"));

        assertThat(result).isEqualTo("1");

    }

}
