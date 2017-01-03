package com.julienviet.aeron;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AeronIPCTestBase extends AeronTestBase {

  protected String dirName;
  protected Vertx vertx;

  @Override
  public void before() {
    super.before();
    dirName = createMediaDriver();
    vertx = Vertx.vertx();
  }

  @Override
  public void after() {
    vertx.close();
    super.after();
  }
}
