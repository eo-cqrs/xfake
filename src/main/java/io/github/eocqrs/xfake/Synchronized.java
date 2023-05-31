package io.github.eocqrs.xfake;

import com.jcabi.xml.XML;
import lombok.RequiredArgsConstructor;
import org.xembly.Directive;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This is synchronized decorator for {@link FkStorage}.
 * <b>This class is thread-safe.</b>
 *
 * @author l3r8yJ
 * @since 0.0.2
 */
@RequiredArgsConstructor
public final class Synchronized implements FkStorage {

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