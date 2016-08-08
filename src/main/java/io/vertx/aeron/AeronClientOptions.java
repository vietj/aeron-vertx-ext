package io.vertx.aeron;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class AeronClientOptions {

  private String directory;

  public AeronClientOptions() {
  }

  public AeronClientOptions(JsonObject json) {
    AeronClientOptionsConverter.fromJson(json, this);
  }

  public String getDirectory() {
    return directory;
  }

  public AeronClientOptions setDirectory(String directory) {
    this.directory = directory;
    return this;
  }
}
