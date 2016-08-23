package io.vertx.aeron.client;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.vertx.aeron.AeronIPCTestBase;
import io.vertx.core.Context;
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
public class SubscriptionTest extends AeronIPCTestBase {

  @Test
  public void testBasic(TestContext context) {
    testBasic(context, 100, 1);
    testBasic(context, 10, 1);
    testBasic(context, 1, 1);
    testBasic(context, 100, 10);
    testBasic(context, 10, 10);
    testBasic(context, 1, 10);
    testBasic(context, 100, 100);
    testBasic(context, 10, 100);
    testBasic(context, 1, 100);
    testBasic(context, 100, 1000);
    testBasic(context, 10, 1000);
    testBasic(context, 1, 1000);
  }

  private void testBasic(TestContext context, int batchSize, int numBuffers) {
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    Async async = context.async();
    client.addSubscription("aeron:ipc", 10, context.asyncAssertSuccess(sub -> {
      sub.batchSize(batchSize);
      AtomicInteger count = new AtomicInteger();
      sub.handler(buff -> {
        assertEquals("HELLO", buff.toString());
        if (count.incrementAndGet() == numBuffers) {
          async.complete();
        }
      });
    }));
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(dirName);
    try (Aeron aeron = Aeron.connect(ctx);
         Publication publication = aeron.addPublication("aeron:ipc", 10))
    {
      for (int i = 0; i < numBuffers;i++) {
        assertTrue(publication.offer(new UnsafeBuffer("HELLO".getBytes())) >= 0);
      }
    }
    async.awaitSuccess(10000);
  }

  @Test
  public void testPauseAtBeginning(TestContext context) {
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    Async async = context.async();
    AtomicInteger count = new AtomicInteger();
    CompletableFuture<Void> resume = new CompletableFuture<>();
    client.addSubscription("aeron:ipc", 10, context.asyncAssertSuccess(sub -> {
      Context ctx = vertx.getOrCreateContext();
      sub.batchSize(1000);
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
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(dirName);
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
