package io.vertx.aeron.impl;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.vertx.aeron.AeronClient;
import io.vertx.aeron.AeronClientOptions;
import io.vertx.aeron.AeronPublication;
import io.vertx.aeron.AeronSubscription;
import io.vertx.aeron.AeronSubscriptionOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronClientImpl implements AeronClient {

  private static final AeronSubscriptionOptions DEFAULT_SUB_OPTIONS = new AeronSubscriptionOptions();

  private final Vertx vertx;
  private final Aeron.Context context;
  private final Aeron aeron;

  public AeronClientImpl(Vertx vertx, AeronClientOptions options) {
    context = new Aeron.Context();
    if (options.getDirectory() != null) {
      context.aeronDirectoryName(options.getDirectory());
    }
    aeron = Aeron.connect(context);
    this.vertx = vertx;
  }

  @Override
  public void addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler) {
    Context ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      Publication pub = aeron.addPublication(channel, streamId);
      pubHandler.handle(Future.succeededFuture(new AeronPublicationImpl(ctx, pub)));
    });
  }

  @Override
  public void addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler) {
    addSubscription(channel, streamId, null, subHandler);
  }

  @Override
  public void addSubscription(String channel, int streamId, AeronSubscriptionOptions options, Handler<AsyncResult<AeronSubscription>> subHandler) {
    AeronSubscriptionOptions effectiveOptions = options != null ? new AeronSubscriptionOptions(options) : DEFAULT_SUB_OPTIONS;
    Context ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      AeronSubscriptionImpl sub = new AeronSubscriptionImpl(ctx, effectiveOptions, aeron.addSubscription(channel, streamId));
      subHandler.handle(Future.succeededFuture(sub));
      sub.read();
    });
  }
}
