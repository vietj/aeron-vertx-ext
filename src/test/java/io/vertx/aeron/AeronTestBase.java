package io.vertx.aeron;

import io.aeron.driver.MediaDriver;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class AeronTestBase {

  protected MediaDriver mediaDriver;

  @Before
  public void before() {
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
    mediaDriver = MediaDriver.launch(new MediaDriver.Context().aeronDirectoryName(dir.getAbsolutePath()));
  }

  @After
  public void after() {
    if (mediaDriver != null) {
      mediaDriver.close();
    }
  }
}
