package io.vertx.aeron.client.impl;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.vertx.aeron.client.AeronClient;
import io.vertx.aeron.client.AeronClientOptions;
import io.vertx.aeron.client.AeronPublication;
import io.vertx.aeron.client.AeronSubscription;
import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronClientImpl implements AeronClient, Closeable {

  private static final Logger log = LoggerFactory.getLogger(AeronClientImpl.class);
  private final VertxInternal vertx;
  private final Aeron aeron;
  private Handler<Throwable> exceptionHandler;

  public AeronClientImpl(VertxInternal vertx, AeronClientOptions options) {
    Aeron.Context aeronContext = new Aeron.Context();
    aeronContext.errorHandler(t -> {
      Handler<Throwable> handler = exceptionHandler();
      if (handler == null) {
        handler = vertx.exceptionHandler();
      }
      if (handler != null) {
        handler.handle(t);
      } else {
        log.error("Unhandled exception", t);
      }
    });
    if (options.getDirectory() != null) {
      aeronContext.aeronDirectoryName(options.getDirectory());
    }
    aeron = Aeron.connect(aeronContext);
    this.vertx = vertx;
    vertx.addCloseHook(this);
  }

  public AeronClientImpl(VertxInternal vertx, Aeron aeron) {
    this.aeron = aeron;
    this.vertx = vertx;
    vertx.addCloseHook(this);
  }

  @Override
  public void close() {
    vertx.removeCloseHook(this);
    doClose();
  }

  private void doClose() {
    aeron.close();
  }

  public void close(Handler<AsyncResult<Void>> completionHandler) {
    try {
      doClose();
    } catch (Throwable t) {
      completionHandler.handle(Future.failedFuture(t));
      return;
    }
    completionHandler.handle(Future.succeededFuture());
  }

  @Override
  public AeronClient addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler) {
    ContextInternal ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      Publication pub = aeron.addPublication(channel, streamId);
      pubHandler.handle(Future.succeededFuture(new AeronPublicationImpl(ctx, pub)));
    });
    return this;
  }

  @Override
  public AeronClient addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler) {
    ContextInternal ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      AeronSubscriptionImpl sub = new AeronSubscriptionImpl(ctx, aeron.addSubscription(channel, streamId));
      subHandler.handle(Future.succeededFuture(sub));
      sub.read();
    });
    return this;
  }

  private synchronized Handler<Throwable> exceptionHandler() {
    return exceptionHandler;
  }

  @Override
  public synchronized AeronClient exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }
}
