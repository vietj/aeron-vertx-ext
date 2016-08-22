package io.vertx.aeron.client;

import io.aeron.driver.FlowControl;
import io.aeron.driver.FlowControlSupplier;
import io.aeron.driver.MaxMulticastFlowControl;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.UnicastFlowControl;
import io.aeron.driver.media.UdpChannel;
import io.vertx.aeron.AeronTestBase;
import io.vertx.aeron.BufferReadStream;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.Pump;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MulticastPublicationTest extends AeronTestBase {

  private Vertx vertx;
  private String senderDirName;
  private String receiver1DirName;
  private String receiver2DirName;

  @Override
  public void before() {
    super.before();
    senderDirName = createMediaDriver(new MediaDriver.Context().multicastFlowControlSupplier(new FlowControlSupplier() {
      @Override
      public FlowControl newInstance(UdpChannel udpChannel, int streamId, long registrationId) {
        return new MaxMulticastFlowControl();
      }
    }));
    receiver1DirName = createMediaDriver();
    receiver2DirName = createMediaDriver();
    vertx = Vertx.vertx();
  }

  @Test()
  public void testFoo(TestContext ctx) throws Exception {

    Async async = ctx.async(3);

    AeronClient senderClient = AeronClient.create(vertx, new AeronClientOptions().setDirectory(senderDirName));
    AeronClient receiver1Client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(receiver1DirName));
    AeronClient receiver2Client = AeronClient.create(vertx, new AeronClientOptions().setDirectory(receiver2DirName));
    String channel = "aeron:udp?endpoint=224.0.1.1:40456";
    AtomicInteger recv1 = new AtomicInteger();
    receiver1Client.addSubscription(channel, 10, ctx.asyncAssertSuccess(sub -> {
      sub.handler(buff -> {
        if (recv1.incrementAndGet() == 50_000) {
          async.countDown();
        }
      });
    }));
    AtomicInteger recv2 = new AtomicInteger();
    receiver2Client.addSubscription(channel, 10, ctx.asyncAssertSuccess(sub -> {
      sub.handler(buff -> {
        if (recv2.incrementAndGet() == 50_000) {
          async.countDown();
        }
      });
    }));
    senderClient.addPublication(channel, 10, ctx.asyncAssertSuccess(pub -> {
      BufferReadStream stream = new BufferReadStream(50_000, 10, 128);
      Pump pump = Pump.pump(stream, pub);
      stream.endHandler(v -> {
        pump.stop();
        async.countDown();
      });
      pump.start();
      stream.send();
    }));
  }
}
