package io.github.eocqrs.xfake;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import org.xembly.Directive;

import java.util.logging.Level;

/**
 * Logging decorator for {@link FkStorage}.
 *
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.1.2
 */
public final class Logged implements FkStorage {

  /**
   * Origin Storage to decorate.
   */
  private final FkStorage origin;
  /**
   * Logging level.
   */
  private final Level level;

  /**
   * Ctor.
   *
   * @param orgn Storage
   * @param lvl  Logging level
   */
  public Logged(final FkStorage orgn, final Level lvl) {
    this.origin = orgn;
    this.level = lvl;
  }

  @Override
  public XML xml() throws Exception {
    Logger.log(this.level, this, "#xml method was called");
    final XML xml = this.origin.xml();
    Logger.log(this.level, this, "XML loaded: %s", xml);
    return xml;
  }

  @Override
  public void apply(final Iterable<Directive> dirs) throws Exception {
    Logger.log(this.level, this, "#apply(%s) was called", dirs);
    this.origin.apply(dirs);
  }
}
