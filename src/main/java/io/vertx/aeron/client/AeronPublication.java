package io.vertx.aeron.client;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronPublication extends WriteStream<Buffer> {

  int DEFAULT_BATCH_SIZE = 100;

  /**
   * Default max queue size in memory 1MB, not in Aeron.
   */
  int DEFAULT_MAX_SIZE = 1024 * 1024;
  int DEFAULT_OFFER_NUM_RETRIES = 200;
  long DEFAULT_OFFER_RETRY_DELAY = 1;
  long DEFAULT_CONNECT_RETRY_DELAY = 200;

  /**
   * Set the number of buffers offered to a Publication.
   *
   * @param size the batch size
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronPublication setBatchSize(int size);

  /**
   * Set the number of retries when a buffer offered to a {@code Publication} resulted in a {@code Publication.BACK_PRESSURED}
   * or an {@code ADMIN_ACTION} result.
   *
   * @param retries the number of retries
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronPublication setOfferRetries(int retries);

  /**
   * Set the delay to reschedule a publiation batch of buffers offered to a {@code Publication.Publication} when
   * the result is {@code Publication.BACK_PRESSURED} or {@code ADMIN_ACTION} and the number of retries has been exceeded.
   *
   * @param delay the delay in milliseconds
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronPublication setOfferRetryDelay(int delay);

  /**
   * Set the delay to reschedule a publication batch when the result is {@code Publication.NOT_CONNECTED}.
   *
   * @param delay the delay in milliseconds
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  AeronPublication setConnectRetryDelay(int delay);

  @Override
  AeronPublication exceptionHandler(Handler<Throwable> handler);

  @Override
  AeronPublication write(Buffer buffer);

  @Override
  AeronPublication setWriteQueueMaxSize(int i);

  @Override
  AeronPublication drainHandler(Handler<Void> handler);

  void close();

}
