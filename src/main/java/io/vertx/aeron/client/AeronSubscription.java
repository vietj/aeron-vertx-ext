package io.vertx.aeron.client;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronSubscription extends ReadStream<Buffer> {

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

}
