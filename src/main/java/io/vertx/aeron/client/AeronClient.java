package io.vertx.aeron.client;

import io.vertx.aeron.client.impl.AeronClientImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronClient {

  static AeronClient create(Vertx vertx, AeronClientOptions options) {
    return new AeronClientImpl((VertxInternal) vertx, options);
  }

  @Fluent
  AeronClient addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler);

  @Fluent
  AeronClient addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler);

  @Fluent
  AeronClient exceptionHandler(Handler<Throwable> handler);

  void close();

}
