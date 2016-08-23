package io.vertx.aeron.client.impl;

import io.aeron.ControlledFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.ControlledFragmentHandler;
import io.vertx.aeron.client.AeronSubscription;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronSubscriptionImpl implements AeronSubscription {

  private final Context context;
  private final Subscription sub;
  private boolean paused;
  private ControlledFragmentHandler fragmentHandler;
  private int pollBatchSize = 100;
  private long pollDelay = 1;

  public AeronSubscriptionImpl(Context context, Subscription sub) {
    this.context = context;
    this.sub = sub;
  }

  @Override
  public AeronSubscription setPollBatchSize(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Poll batch size must be >= 1");
    }
    pollBatchSize = size;
    return this;
  }

  @Override
  public AeronSubscription setPollDelay(long delay) {
    if (delay < 1) {
      throw new IllegalArgumentException("Poll delay must be >= 1");
    }
    pollDelay = delay;
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
      sub.controlledPoll(fragmentHandler, pollBatchSize);
      if (!paused) {
        context.owner().setTimer(pollDelay, v -> {
          read();
        });
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
