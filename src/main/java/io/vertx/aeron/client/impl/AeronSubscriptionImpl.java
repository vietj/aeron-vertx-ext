package io.vertx.aeron.client.impl;

import io.aeron.ControlledFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.ControlledFragmentHandler;
import io.vertx.aeron.client.AeronSubscription;
import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.ContextInternal;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class AeronSubscriptionImpl implements AeronSubscription, Closeable {

  private static final int NUM_BUFFER_PER_POLL = 10;

  private final ContextInternal context;
  private final Subscription sub;
  private boolean closed;
  private boolean paused;
  private ControlledFragmentHandler fragmentHandler;
  private int batchSize;
  private int numBatch;
  private long intervalDelay = DEFAULT_BATCH_INTERVAL_DELAY;
  private TimeUnit intervalUnit = DEFAULT_BATCH_INTERVAL_UNIT;

  AeronSubscriptionImpl(ContextInternal context, Subscription sub) {
    this.context = context;
    this.sub = sub;
    batchSize(DEFAULT_BATCH_SIZE);
    context.addCloseHook(this);
  }

  @Override
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    try {
      doClose();
    } catch (Throwable t) {
      completionHandler.handle(Future.failedFuture(t));
      return;
    }
    completionHandler.handle(Future.succeededFuture());
  }

  private void doClose() {
    if (!sub.isClosed()) {
      sub.close();
    }
  }

  boolean isClosed() {
    return sub.isClosed();
  }

  @Override
  public void close() {
    context.removeCloseHook(this);
    doClose();
  }

  @Override
  public AeronSubscription batchSize(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Poll batch size must be >= 1");
    }
    batchSize = size;
    numBatch = batchSize / NUM_BUFFER_PER_POLL;
    return this;
  }

  @Override
  public AeronSubscription batchInterval(long delay, TimeUnit unit) {
    if (delay < 1) {
      throw new IllegalArgumentException("Poll delay must be >= 1");
    }
    intervalDelay = delay;
    intervalUnit = unit;
    return this;
  }

  @Override
  public AeronSubscription exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  @Override
  public AeronSubscription handler(Handler<Buffer> handler) {
    if (handler != null) {
      fragmentHandler = new ControlledFragmentAssembler((buffer, offset, length, header) -> {
        byte[] bytes = new byte[length];
        buffer.getBytes(offset, bytes);
        handler.handle(Buffer.buffer(bytes));
        return paused ? ControlledFragmentHandler.Action.BREAK : ControlledFragmentHandler.Action.COMMIT;
      });
    } else {
      fragmentHandler = new ControlledFragmentAssembler((buffer, offset, length, header) -> paused ? ControlledFragmentHandler.Action.BREAK : ControlledFragmentHandler.Action.COMMIT);
    }
    return this;
  }

  void read() {
    if (!paused) {
      if (numBatch-- > 0) {
        sub.controlledPoll(fragmentHandler, NUM_BUFFER_PER_POLL);
        if (!paused) {
          context.runOnContext(v -> read());
        }
      } else {
        numBatch = batchSize / NUM_BUFFER_PER_POLL;
        sub.controlledPoll(fragmentHandler, batchSize % NUM_BUFFER_PER_POLL);
        if (!paused) {
          context.nettyEventLoop().schedule(this::read, intervalDelay, intervalUnit);
        }
      }
    }
  }

  @Override
  public AeronSubscription pause() {
    if (!paused) {
      paused = true;
    }
    return this;
  }

  @Override
  public AeronSubscription resume() {
    if (paused) {
      paused = false;
      context.runOnContext(v -> {
        read();
      });
    }
    return this;
  }

  @Override
  public AeronSubscription endHandler(Handler<Void> endHandler) {
    return this;
  }
}
