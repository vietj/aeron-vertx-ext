package io.vertx.aeron.impl;

import io.aeron.Publication;
import io.vertx.aeron.AeronPublication;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class AeronPublicationImpl implements AeronPublication {

  private int maxSize = DEFAULT_MAX_SIZE;
  private int batchSize = DEFAULT_BATCH_SIZE;
  private int offerRetries = DEFAULT_OFFER_NUM_RETRIES;
  private long offerRetryDelay = DEFAULT_OFFER_RETRY_DELAY;
  private long connectRetryDelay = DEFAULT_CONNECT_RETRY_DELAY;

  private final Context context;
  private final Vertx vertx;
  private final Publication pub;
  private final ArrayDeque<DirectBuffer> pending = new ArrayDeque<>();
  private int pendingSize; // Pending size in bytes
  private long timerID = -1;
  private Handler<Void> drainHandler;
  private Handler<Throwable> exceptionHandler;

  AeronPublicationImpl(Context context, Publication pub) {
    this.context = context;
    this.vertx = context.owner();
    this.pub = pub;
  }

  @Override
  public AeronPublication exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public AeronPublication write(Buffer buffer) {
    pending.add(new UnsafeBuffer(buffer.getBytes()));
    pendingSize += buffer.length();
    checkPending();
    return this;
  }

  private void schedule(long delay, boolean reschedule) {
    if (timerID >= 0) {
      if (reschedule) {
        vertx.cancelTimer(timerID);
      } else {
        return;
      }
    }
    timerID = vertx.setTimer(delay, id -> {
      timerID = -1;
      checkPending();
    });
  }

  private void checkPending() {
    int num = batchSize;
    while (!pending.isEmpty() && num-- > 0) {
      DirectBuffer current = pending.peek();
      long result = pub.offer(current);
      if (result < 0L) {
        int retries = offerRetries;
        while (result < 0L) {
          if (result == Publication.BACK_PRESSURED || result == Publication.ADMIN_ACTION) {
            if (retries-- > 0) {
              Thread.yield();
              result = pub.offer(current);
            } else {
              schedule(offerRetryDelay, true);
              return;
            }
          } else if (result == Publication.NOT_CONNECTED) {
            schedule(connectRetryDelay, true);
            return;
          } else {
            Handler<Throwable> handler = exceptionHandler;
            if (handler != null) {
              ClosedChannelException err = new ClosedChannelException();
              vertx.runOnContext(v -> {
                handler.handle(err);
              });
            }
            return;
          }
        }
      }
      pending.remove();
      pendingSize -= current.capacity();
    }
    if (pending.size() > 0) {
      schedule(offerRetryDelay, false);
    }
    Handler<Void> h = drainHandler;
    if (pendingSize < maxSize / 2 &&  h != null) {
      drainHandler = null;
      h.handle(null);
    }
  }

  @Override
  public AeronPublication setWriteQueueMaxSize(int size) {
    if (maxSize < 1) {
      throw new IllegalArgumentException();
    }
    maxSize = size;
    return this;
  }

  @Override
  public AeronPublication drainHandler(Handler<Void> handler) {
    drainHandler = handler;
    return null;
  }

  @Override
  public void end() {
    pub.close();
  }

  @Override
  public boolean writeQueueFull() {
    return pendingSize > maxSize;
  }

  public AeronPublication setBatchSize(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Batch size must be > 0");
    }
    this.batchSize = size;
    return this;
  }

  public AeronPublication setOfferRetries(int retries) {
    if (retries < 0) {
      throw new IllegalArgumentException("Offer retries must be >= 0");
    }
    this.offerRetries = retries;
    return this;
  }

  public AeronPublication setOfferRetryDelay(int delay) {
    if (delay < 1) {
      throw new IllegalArgumentException("Offer retry delay must be > 0");
    }
    this.offerRetryDelay = delay;
    return this;
  }

  public AeronPublication setConnectRetryDelay(int delay) {
    if (delay < 1) {
      throw new IllegalArgumentException("Connect retry delay must be > 0");
    }
    this.connectRetryDelay = delay;
    return this;
  }
}
