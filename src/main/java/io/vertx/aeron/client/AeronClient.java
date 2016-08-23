package io.vertx.aeron.client;

import io.vertx.aeron.client.impl.AeronClientImpl;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronClient {

  static AeronClient create(Vertx vertx, AeronClientOptions options) {
    return new AeronClientImpl(vertx, options);
  }

  void addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler);

  void addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler);

}
