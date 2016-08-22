package io.vertx.aeron;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferReadStream implements ReadStream<Buffer> {

  private final Buffer data;
  private final long numBuffers;
  private final long batchSize;
  private final Context context = Vertx.currentContext();
  private boolean paused;
  private Handler<Buffer> dataHandler;
  private long sentBuffers = 0;
  private Handler<Void> endHandler;

  public BufferReadStream(long numBuffers, long batchSize, int chunkSize) {
    this.data = Buffer.buffer(new byte[chunkSize]);
    this.numBuffers = numBuffers;
    this.batchSize = batchSize;
  }

  @Override
  public ReadStream<Buffer> exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  @Override
  public ReadStream<Buffer> handler(Handler<Buffer> handler) {
    dataHandler = handler;
    return this;
  }

  public void send() {
    for (int num = 0;num <= batchSize && !paused;num++) {
      if (num < batchSize) {
        if (sentBuffers < numBuffers) {
          sentBuffers++;
          dataHandler.handle(data);
        } else if (sentBuffers == numBuffers) {
          sentBuffers++;
          if (endHandler != null) {
            endHandler.handle(null);
          }
        } else {
          return;
        }
      } else {
        context.owner().setTimer(1, id -> {
          send();
        });
      }
    }
  }

  @Override
  public ReadStream<Buffer> pause() {
    paused = true;
    return this;
  }

  @Override
  public ReadStream<Buffer> resume() {
    paused = false;
    send();
    return this;
  }

  @Override
  public ReadStream<Buffer> endHandler(Handler<Void> handler) {
    endHandler = handler;
    return this;
  }
}
