package io.vertx.aeron.client;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronSubscription extends ReadStream<Buffer> {

  int DEFAULT_BATCH_SIZE = 100;
  long DEFAULT_BATCH_DELAY = 1;

  /**
   * Set the number of buffers polled from the {@code Subscription}.
   *
   * @param size the number of buffers
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronSubscription setBatchSize(int size);

  /**
   * Set the delay between polls of the {@code Subscription}.
   *
   * @param delay the delay in milliseconds
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronSubscription setBatchDelay(long delay);

  @Override
  AeronSubscription exceptionHandler(Handler<Throwable> handler);

  @Override
  AeronSubscription handler(Handler<Buffer> handler);

  @Override
  AeronSubscription pause();

  @Override
  AeronSubscription resume();

  @Override
  AeronSubscription endHandler(Handler<Void> endHandler);

  void close();

}
