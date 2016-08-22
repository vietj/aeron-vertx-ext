package io.vertx.aeron.client.impl;

import io.aeron.ControlledFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.ControlledFragmentHandler;
import io.vertx.aeron.client.AeronSubscription;
import io.vertx.aeron.client.AeronSubscriptionOptions;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronSubscriptionImpl implements AeronSubscription {

  private final AeronSubscriptionOptions options;
  private final Context context;
  private final Subscription sub;
  private boolean paused;
  private ControlledFragmentHandler fragmentHandler;

  public AeronSubscriptionImpl(Context context, AeronSubscriptionOptions options, Subscription sub) {
    this.context = context;
    this.sub = sub;
    this.options = options;
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
      sub.controlledPoll(fragmentHandler, 100);
      if (!paused) {
        context.owner().setTimer(1, v -> {
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
