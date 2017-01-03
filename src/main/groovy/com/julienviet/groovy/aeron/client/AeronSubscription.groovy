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
import java.util.concurrent.TimeUnit
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.streams.ReadStream
import io.vertx.core.Handler
/**
*/
@CompileStatic
public class AeronSubscription implements ReadStream<Buffer> {
  private final def com.julienviet.aeron.client.AeronSubscription delegate;
  public AeronSubscription(Object delegate) {
    this.delegate = (com.julienviet.aeron.client.AeronSubscription) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Set the number of buffers polled from the <code>Subscription</code>.
   * @param size the number of buffers
   * @return a reference to this, so the API can be used fluently
   */
  public AeronSubscription batchSize(int size) {
    delegate.batchSize(size);
    return this;
  }
  /**
   * Set the delay between 2 polls of the <code>Subscription</code>.
   * @param delay the delay between two intervals
   * @param unit the interval time unit
   * @return a reference to this, so the API can be used fluently
   */
  public AeronSubscription batchInterval(long delay, TimeUnit unit) {
    delegate.batchInterval(delay, unit);
    return this;
  }
  /**
   * Close the subscription.
   */
  public void close() {
    delegate.close();
  }
  public AeronSubscription exceptionHandler(Handler<Throwable> handler) {
    ((io.vertx.core.streams.StreamBase) delegate).exceptionHandler(handler);
    return this;
  }
  public AeronSubscription handler(Handler<Buffer> handler) {
    ((io.vertx.core.streams.ReadStream) delegate).handler(handler != null ? new Handler<io.vertx.core.buffer.Buffer>(){
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.core.buffer.Buffer.class));
      }
    } : null);
    return this;
  }
  public AeronSubscription pause() {
    ((io.vertx.core.streams.ReadStream) delegate).pause();
    return this;
  }
  public AeronSubscription resume() {
    ((io.vertx.core.streams.ReadStream) delegate).resume();
    return this;
  }
  public AeronSubscription endHandler(Handler<Void> endHandler) {
    ((io.vertx.core.streams.ReadStream) delegate).endHandler(endHandler);
    return this;
  }
}
