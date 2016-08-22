package io.vertx.aeron;

import io.aeron.Aeron;
import io.aeron.Subscription;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class PublicationTest extends AeronTestBase {

  @Test
  public void testBasic() throws Exception {
    Vertx vertx = Vertx.vertx();
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(mediaDriver.aeronDirectoryName()));
    client.addPublication("aeron:ipc", 10, ar -> {
      AeronPublication pub = ar.result();
      pub.write(Buffer.buffer("Hello"));
      pub.write(Buffer.buffer("World"));
    });
    List<String> received = new ArrayList<>();
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(mediaDriver.aeronDirectoryName());
    try (Aeron aeron = Aeron.connect(ctx);
         Subscription subscription = aeron.addSubscription("aeron:ipc", 10))
    {
      int numRec = 0;
      while (numRec < 2) {
        int result = subscription.poll((buffer, offset, length, header) -> {
          byte[] data = new byte[length];
          buffer.getBytes(offset, data);
          received.add(new String(data));
        }, 10);
        if (result > 0) {
          numRec += result;
        } else {
          Thread.sleep(1);
        }
      }
    }
    assertEquals(Arrays.asList("Hello", "World"), received);
  }

  @Test
  public void testBufferWhenNoSubscription(TestContext context) throws Exception {
    Vertx vertx = Vertx.vertx();
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(mediaDriver.aeronDirectoryName()));
    Async async = context.async();
    AtomicInteger expected = new AtomicInteger();
    Async drainedEvent = context.async();
    client.addPublication("aeron:ipc", 10, ar -> {
      AeronPublication pub = ar.result();
      while (!pub.writeQueueFull()) {
        pub.write(Buffer.buffer("HELLO"));
        expected.incrementAndGet();
      }
      AtomicBoolean drained = new AtomicBoolean();
      pub.drainHandler(v -> {
        context.assertTrue(drained.compareAndSet(false, true));
        drainedEvent.complete();
      });
      async.complete();
    });
    Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(mediaDriver.aeronDirectoryName());
    try (Aeron aeron = Aeron.connect(ctx)) {
      async.awaitSuccess(10000);
      try (Subscription subscription = aeron.addSubscription("aeron:ipc", 10)) {
        while (expected.get() > 0) {
          int result = subscription.poll((buffer, offset, length, header) -> {
            byte[] data = new byte[length];
            buffer.getBytes(offset, data);
            assertEquals("HELLO", new String(data));
          }, 10);
          if (result > 0) {
            expected.getAndAdd(-result);
          } else {
            Thread.sleep(1);
          }
        }
      }
    }
    drainedEvent.await(10000);
  }

  @Test
  public void testClosed(TestContext context) throws Exception {
    Vertx vertx = Vertx.vertx();
    AeronClient client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(mediaDriver.aeronDirectoryName()));
    Async async = context.async();
    client.addPublication("aeron:ipc", 10, ar -> {
      AeronPublication pub = ar.result();
      pub.exceptionHandler(err -> {
        context.assertTrue(err instanceof ClosedChannelException);
        async.complete();
      });
      pub.end();
      pub.write(Buffer.buffer("foobar"));
    });
  }
}
