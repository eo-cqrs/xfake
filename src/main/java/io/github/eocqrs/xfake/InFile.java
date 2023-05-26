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

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.cactoos.io.TeeInput;
import org.cactoos.scalar.LengthOf;
import org.cactoos.text.TextOf;
import org.xembly.Directive;
import org.xembly.Xembler;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
/*
 * @todo #22:45m/DEV Test Thread safety
 * We need to introduce tests for Thread safety
 */

/**
 * Storage in file.
 *
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.0.0
 */
public final class InFile implements FkStorage {
  /**
   * File name.
   */
  private final transient String name;

  /**
   * Ctor.
   *
   * @param name File name to create
   * @param root root XML node
   * @throws Exception When something went wrong.
   */
  public InFile(final String name, final String root) throws Exception {
    this(File.createTempFile(name, ".xml"), root);
    new File(this.name).deleteOnExit();
  }

  /**
   * Ctor.
   *
   * @param file File
   * @param root root XML node
   * @throws Exception When something went wrong.
   */
  public InFile(final File file, final String root) throws Exception {
    new LengthOf(
      new TeeInput(
        root,
        file,
        StandardCharsets.UTF_8
      )
    ).value();
    this.name = file.getAbsolutePath();
  }

  @Override
  public XML xml() throws Exception {
    return new XMLDocument(
      new TextOf(
        new File(
          this.name
        )
      ).asString()
    );
  }

  @Override
  public void apply(final Iterable<Directive> dirs) throws Exception {
    new LengthOf(
      new TeeInput(
        new XMLDocument(
          new Xembler(
            dirs
          ).applyQuietly(this.xml().node())
        ).toString(),
        new File(
          this.name
        ),
        StandardCharsets.UTF_8
      )
    ).value();
  }
}
