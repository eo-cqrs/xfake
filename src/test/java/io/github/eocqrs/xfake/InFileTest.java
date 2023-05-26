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

import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.xembly.Directives;

/**
 * Test case for {@link InFile}
 *
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.0.0
 */
final class InFileTest {

  private static final int N_THREADS = Runtime.getRuntime().availableProcessors();

  private final ExecutorService executors = Executors.newCachedThreadPool();

  private final CountDownLatch latch = new CountDownLatch(1);

  @Test
  void createsStorageInXmlFile() throws Exception {
    final FkStorage storage = new InFile("fake-test", "<broker/>");
    MatcherAssert.assertThat(
      "Storage has root <broker> node",
      storage.xml().nodes("broker").isEmpty(),
      Matchers.equalTo(false)
    );
  }

  @Test
  void appliesDirectives() throws Exception {
    final FkStorage storage = new InFile("fake-test", "<broker/>");
    storage.apply(
      new Directives()
        .xpath("/broker")
        .addIf("servers")
    );
    MatcherAssert.assertThat(
      "XML has right format",
      storage.xml().nodes("broker/servers").isEmpty(),
      Matchers.equalTo(false)
    );
  }

  @Test
  void readsAndWritesConcurrently() throws Exception {
    final Stack<Integer> values = new Stack<>();
    IntStream.range(0, InFileTest.N_THREADS).forEach(values::push);
    final FkStorage storage = new FkSynchronizedStorage(
      new InFile("fake", "<stack/>")
    );
    for (int idx = 0; idx < InFileTest.N_THREADS; idx++) {
      this.executors.submit(
        () -> {
          this.latch.await();
          storage.apply(
            new Directives()
              .xpath("/stack")
              .add("item")
              .set(values.pop())
          );
          return 0;
        }
      );
    }
    this.latch.countDown();
    this.executors.awaitTermination(2L, TimeUnit.SECONDS);
    MatcherAssert.assertThat(
      "Has size equal to stack size",
      storage.xml().nodes("/stack/item"),
      Matchers.hasSize(InFileTest.N_THREADS)
    );
  }
}
