/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.aeron;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.aeron.AeronClientOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.aeron.AeronClientOptions} original class using Vert.x codegen.
 */
public class AeronClientOptionsConverter {

  public static void fromJson(JsonObject json, AeronClientOptions obj) {
    if (json.getValue("directory") instanceof String) {
      obj.setDirectory((String)json.getValue("directory"));
    }
  }

  public static void toJson(AeronClientOptions obj, JsonObject json) {
    if (obj.getDirectory() != null) {
      json.put("directory", obj.getDirectory());
    }
  }
}