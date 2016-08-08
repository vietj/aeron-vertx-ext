package io.vertx.aeron.impl;

import io.aeron.Publication;
import io.vertx.aeron.AeronPublication;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.util.ArrayDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class AeronPublicationImpl implements AeronPublication {

  private static final int BATCH_SIZE = 100;

  private int maxSize = 1024 * 1024; // 1 MB
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

  private void checkPending() {
    int num = BATCH_SIZE;
    while (!pending.isEmpty() && num-- > 0) {
      DirectBuffer peek = pending.peek();
      long result = pub.offer(peek);
      if (result < 0L) {
        break;
      } else {
        pending.remove();
        pendingSize -= peek.capacity();
      }
    }
    if (pending.size() > 0 && timerID < 0) {
      // The schedule strategy should be configurable and also depends on how many message we sent
      // 1ms will cause 1ms batch delay in pumps
      // plain run on context may consume too much CPU
      timerID = vertx.setTimer(1, id -> {
        timerID = -1;
        checkPending();
      });
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
  }

  @Override
  public boolean writeQueueFull() {
    return pendingSize > maxSize;
  }
}
