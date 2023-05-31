package io.github.eocqrs.xfake;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.xembly.Directives;

import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * Test case for {@link Synchronized}.
 *
 * @author l3r8yJ
 * @since 0.0.2
 */
final class SynchronizedTest {

  private static final int N_THREADS = Runtime.getRuntime().availableProcessors();

  private final ExecutorService executors = Executors.newCachedThreadPool();

  private final CountDownLatch latch = new CountDownLatch(1);

  @Test
  void readsAndWritesConcurrently() throws Exception {
    final Stack<Integer> values = new Stack<>();
    IntStream.range(0, SynchronizedTest.N_THREADS).forEach(values::push);
    final FkStorage storage = new Synchronized(
      new InFile("fake", "<stack/>")
    );
    for (int idx = 0; idx < SynchronizedTest.N_THREADS; idx++) {
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
    final boolean failed = this.executors.awaitTermination(
      (long) (SynchronizedTest.N_THREADS / 2), TimeUnit.SECONDS
    );
    if (failed) {
      throw new TimeoutException("FkSynchronizedStorageTest#readsAndWritesConcurrently() failed");
    }
    MatcherAssert.assertThat(
      "Has size equal to stack size",
      storage.xml().nodes("/stack/item"),
      Matchers.hasSize(SynchronizedTest.N_THREADS)
    );
  }
}