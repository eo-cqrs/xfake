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

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.cactoos.io.TeeInput;
import org.cactoos.scalar.LengthOf;
import org.cactoos.text.TextOf;
import org.xembly.Directive;
import org.xembly.Xembler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
   * Lock.
   */
  private final transient ImmutableReentrantLock lock =
    new ImmutableReentrantLock();

  /**
   * Read-Write Lock.
   */
  private final transient ReentrantReadWriteLock rw =
    new ReentrantReadWriteLock();

  /**
   * Ctor.
   *
   * @param nm   File name to create
   * @param root root XML node
   * @throws Exception When something went wrong.
   */
  public InFile(final String nm, final String root) throws Exception {
    this(File.createTempFile(nm, ".xml"), root);
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
    this.rw.readLock().lock();
    try {
      return new XMLDocument(
        new TextOf(
          new File(
            this.name
          )
        ).asString()
      );
    } finally {
      this.rw.readLock().unlock();
    }
  }

  @Override
  public void apply(final Iterable<Directive> dirs) throws Exception {
    synchronized (this.name) {
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

  @Override
  public void lock() {
    this.lock.lock();
  }

  @Override
  public void unlock() {
    this.lock.unlock();
  }
}
