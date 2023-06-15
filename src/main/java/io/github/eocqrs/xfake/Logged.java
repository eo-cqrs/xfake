/*
 *  Copyright (c) 2023 Aliaksei Bialiauski, EO-CQRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
