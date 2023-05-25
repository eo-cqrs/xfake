package io.github.eocqrs.xfake;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ImmutableReentrantLock}
 *
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.0.0
 */
final class ImmutableReentrantLockTest {

  @Test
  void locksAndUnlocksWithoutException() {
    final ImmutableReentrantLock lock = new ImmutableReentrantLock();
    Assertions.assertDoesNotThrow(
      lock::lock
    );
    Assertions.assertDoesNotThrow(
      lock::unlock
    );
  }
}
