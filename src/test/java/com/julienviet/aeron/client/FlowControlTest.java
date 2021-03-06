package com.julienviet.aeron.client;

import com.julienviet.aeron.AeronIPCTestBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowControlTest extends AeronIPCTestBase {
  
  @Test
  public void testSimple(TestContext context) {
    AeronClient pubClient = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    AeronClient subClient = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    final int numBatches = 10000;
    final int batchSize = 200;
    Async done = context.async();
    Async subSync = context.async();
    subClient.addSubscription("aeron:ipc", 10, ar -> {
      AeronSubscription consumer = ar.result();
      consumer.batchSize(1000);
      AtomicInteger cnt = new AtomicInteger();
      consumer.handler(msg -> {
        int c = cnt.incrementAndGet();
        if (c == numBatches * batchSize) {
          done.complete();
        }
      });
      subSync.complete();
    });
    subSync.awaitSuccess(10000);
    pubClient.addPublication("aeron:ipc", 10, ar -> {
      WriteStream<Buffer> prod = ar.result();
      prod.setWriteQueueMaxSize(batchSize);
      sendBatch(prod, 0, numBatches, batchSize);
    });
  }

  @Test
  public void testPauseResume(TestContext context) {
    AeronClient pubClient = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    AeronClient subClient = AeronClient.create(vertx, new AeronClientOptions().setDirectory(dirName));
    final int numBatches = 10000;
    final int batchSize = 200;
    Async done = context.async();
    Async subSync = context.async();
    subClient.addSubscription("aeron:ipc", 10, ar -> {
      AeronSubscription consumer = ar.result();
      consumer.batchSize(1000);
      AtomicInteger cnt = new AtomicInteger();
      consumer.handler(msg -> {
        int c = cnt.incrementAndGet();
        if (c == numBatches * batchSize) {
          done.complete();
        } else {
          if (cnt.get() % 10000 == 0) {
            consumer.pause();
            vertx.setTimer(5, id -> {
              consumer.resume();
            });
          }
        }
      });
      subSync.complete();
    });
    subSync.awaitSuccess(10000);
    pubClient.addPublication("aeron:ipc", 10, ar -> {
      WriteStream<Buffer> prod = ar.result();
      prod.setWriteQueueMaxSize(batchSize);
      sendBatch(prod, 0, numBatches, batchSize);
    });
  }

  private void sendBatch(WriteStream<Buffer> prod, int batchNumber, int numBatches, int batchSize) {
    while (batchNumber < numBatches) {
      for (int i = 0; i < batchSize; i++) {
        prod.write(Buffer.buffer("message-" + i));
      }
      if (prod.writeQueueFull()) {
        int next = batchNumber + 1;
        prod.drainHandler(v -> {
          sendBatch(prod, next, numBatches, batchSize);
        });
        return;
      } else {
        batchNumber++;
      }
    }
  }
}
