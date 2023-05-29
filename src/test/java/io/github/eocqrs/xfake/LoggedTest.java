package io.github.eocqrs.xfake;

import com.jcabi.log.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xembly.Directives;

import java.util.logging.Level;

/**
 * Test case for {@link Logged}.
 *
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.1.2
 */
final class LoggedTest {

  @Test
  void writesXmlLog() throws Exception {
    final Level lvl = Level.INFO;
    final FkStorage storage =
      new Logged(
        new Synchronized(
          new InFile("logged-test", "<fake/>")
        ),
        lvl
      );
    Assertions.assertDoesNotThrow(storage::xml);
    Assertions.assertDoesNotThrow(() -> storage.apply(
        new Directives()
          .xpath("/fake")
          .addIf("ping")
      )
    );
    MatcherAssert.assertThat(
      "Logging is enabled",
      Logger.isEnabled(lvl, Logged.class),
      Matchers.is(true)
    );
  }
}
