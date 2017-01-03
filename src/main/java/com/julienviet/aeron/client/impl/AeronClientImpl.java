package com.julienviet.aeron.client.impl;

import io.aeron.Aeron;
import com.julienviet.aeron.client.AeronClient;
import com.julienviet.aeron.client.AeronClientOptions;
import com.julienviet.aeron.client.AeronPublication;
import com.julienviet.aeron.client.AeronSubscription;
import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronClientImpl implements AeronClient, Closeable {

  private static final Logger log = LoggerFactory.getLogger(AeronClientImpl.class);
  private final VertxInternal vertx;
  private final Aeron aeron;
  private Handler<Throwable> exceptionHandler;
  private final ConcurrentHashSet<AeronPublicationImpl> pubs = new ConcurrentHashSet<>();
  private final ConcurrentHashSet<AeronSubscriptionImpl> subs = new ConcurrentHashSet<>();

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
    new ArrayList<>(pubs).forEach(AeronPublicationImpl::close);
    new ArrayList<>(subs).forEach(AeronSubscriptionImpl::close);
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
      Future<AeronPublication> fut = Future.future();
      try {
        AeronPublicationImpl pub = new AeronPublicationImpl(ctx, aeron.addPublication(channel, streamId));
        pubs.add(pub);
        fut.complete(pub);
      } catch (Throwable e) {
        fut.fail(e);
      }
      pubHandler.handle(fut);
    });
    return this;
  }

  @Override
  public AeronClient addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler) {
    ContextInternal ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      AeronSubscriptionImpl sub = null;
      Future<AeronSubscription> fut = Future.future();
      try {
        subs.add(sub = new AeronSubscriptionImpl(ctx, aeron.addSubscription(channel, streamId)));
        fut.complete(sub);
      } catch (Throwable e) {
        fut.fail(e);
      }
      subHandler.handle(fut);
      if (sub != null && !sub.isClosed()) {
        sub.read();
      }
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
