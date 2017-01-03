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

package com.julienviet.groovy.aeron.client;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx
import com.julienviet.aeron.client.AeronClientOptions
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
*/
@CompileStatic
public class AeronClient {
  private final def com.julienviet.aeron.client.AeronClient delegate;
  public AeronClient(Object delegate) {
    this.delegate = (com.julienviet.aeron.client.AeronClient) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static AeronClient create(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(com.julienviet.aeron.client.AeronClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new com.julienviet.aeron.client.AeronClientOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null), com.julienviet.groovy.aeron.client.AeronClient.class);
    return ret;
  }
  public static AeronClient create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(com.julienviet.aeron.client.AeronClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), com.julienviet.groovy.aeron.client.AeronClient.class);
    return ret;
  }
  public AeronClient addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler) {
    delegate.addPublication(channel, streamId, pubHandler != null ? new Handler<AsyncResult<com.julienviet.aeron.client.AeronPublication>>() {
      public void handle(AsyncResult<com.julienviet.aeron.client.AeronPublication> ar) {
        if (ar.succeeded()) {
          pubHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), com.julienviet.groovy.aeron.client.AeronPublication.class)));
        } else {
          pubHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public AeronClient addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler) {
    delegate.addSubscription(channel, streamId, subHandler != null ? new Handler<AsyncResult<com.julienviet.aeron.client.AeronSubscription>>() {
      public void handle(AsyncResult<com.julienviet.aeron.client.AeronSubscription> ar) {
        if (ar.succeeded()) {
          subHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), com.julienviet.groovy.aeron.client.AeronSubscription.class)));
        } else {
          subHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public AeronClient exceptionHandler(Handler<Throwable> handler) {
    delegate.exceptionHandler(handler);
    return this;
  }
  /**
   * Close the client.
   */
  public void close() {
    delegate.close();
  }
}
