package com.julienviet.aeron;

import io.aeron.driver.MediaDriver;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class AeronTestBase {

  protected MediaDriver[] mediaDrivers;

  @Before
  public void before() {
    mediaDrivers = new MediaDriver[0];
  }

  protected String createMediaDriver() {
    return createMediaDriver(new MediaDriver.Context());
  }

  public static Buffer randomBuffer(int size) {
    byte[] bytes = new byte[size];
    new Random().nextBytes(bytes);
    return Buffer.buffer(bytes);
  }

  protected String createMediaDriver(MediaDriver.Context context) {
    File dir;
    int count = 0;
    while (true) {
      dir = new File(new File("target"), "aeron-" + count);
      if (!dir.exists() && dir.mkdirs()) {
        break;
      }
      if (count < 100000) {
        count++;
      } else {
        throw new AssertionError();
      }
    }
    String dirName = dir.getAbsolutePath();
    mediaDrivers = Arrays.copyOf(mediaDrivers, mediaDrivers.length + 1);
    mediaDrivers[mediaDrivers.length - 1] = MediaDriver.launch(context.aeronDirectoryName(dirName));
    return dirName;
  }

  @After
  public void after() {
    for (MediaDriver driver : mediaDrivers) {
      driver.close();
    }
  }
}
