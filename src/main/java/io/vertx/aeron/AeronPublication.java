package io.vertx.aeron;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface AeronPublication extends WriteStream<Buffer> {

  @Override
  AeronPublication exceptionHandler(Handler<Throwable> handler);

  @Override
  AeronPublication write(Buffer buffer);

  @Override
  AeronPublication setWriteQueueMaxSize(int i);

  @Override
  AeronPublication drainHandler(Handler<Void> handler);

}
