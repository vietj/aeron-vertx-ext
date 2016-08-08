package io.vertx.aeron;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SubscriptionTest extends AeronTestBase {

  @Test
  public void testBasic(TestContext context) {
    Vertx vertx = Vertx.vertx();
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(mediaDriver.aeronDirectoryName()));
    Async async = context.async();
    client.addSubscription("aeron:ipc", 10, context.asyncAssertSuccess(sub -> {
      sub.handler(buff -> {
        assertEquals("HELLO", buff.toString());
        async.complete();
      });
    }));
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(mediaDriver.aeronDirectoryName());
    try (Aeron aeron = Aeron.connect(ctx);
         Publication publication = aeron.addPublication("aeron:ipc", 10))
    {
      assertTrue(publication.offer(new UnsafeBuffer("HELLO".getBytes())) >= 0);
    }
  }

  @Test
  public void testPauseAtBeginning(TestContext context) {
    Vertx vertx = Vertx.vertx();
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(mediaDriver.aeronDirectoryName()));
    Async async = context.async();
    AtomicInteger count = new AtomicInteger();
    CompletableFuture<Void> resume = new CompletableFuture<>();
    client.addSubscription("aeron:ipc", 10, context.asyncAssertSuccess(sub -> {
      Context ctx = vertx.getOrCreateContext();
      sub.handler(buff -> {
        assertEquals("HELLO", buff.toString());
        if (count.decrementAndGet() == 0) {
          async.complete();
        }
      });
      sub.pause();
      resume.thenAccept(v1 -> {
        ctx.runOnContext(v2 -> {
          sub.resume();
        });
      });
    }));
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(mediaDriver.aeronDirectoryName());
    try (Aeron aeron = Aeron.connect(ctx);
         Publication publication = aeron.addPublication("aeron:ipc", 10))
    {
      while (true) {
        if (publication.offer(new UnsafeBuffer("HELLO".getBytes())) < 0) {
          break;
        }
        count.incrementAndGet();
      }
      resume.complete(null);
    }
  }
}
