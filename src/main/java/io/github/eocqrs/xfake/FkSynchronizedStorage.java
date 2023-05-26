package io.github.eocqrs.xfake;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.xembly.Directive;

import com.jcabi.xml.XML;

import lombok.RequiredArgsConstructor;

/**
 * This is synchronized decorator for {@link FkStorage}.
 * <b>This class is thread-safe.</b>
 *
 * @since 0.0.2
 * @author l3r8yJ
 */
@RequiredArgsConstructor
public final class FkSynchronizedStorage implements FkStorage {

  /**
   * Lock.
   */
  private final transient ReadWriteLock rwl = new ReentrantReadWriteLock();

  /**
   * Wrapped storage.
   */
  private final FkStorage origin;

  @Override
  public XML xml() throws Exception {
    this.rwl.readLock().lock();
    try {
      return this.origin.xml();
    } finally {
      this.rwl.readLock().unlock();
    }
  }

  @Override
  public void apply(final Iterable<Directive> dirs) throws Exception {
    this.rwl.writeLock().lock();
    try {
      this.origin.apply(dirs);
    } finally {
      this.rwl.writeLock().unlock();
    }
  }
}
