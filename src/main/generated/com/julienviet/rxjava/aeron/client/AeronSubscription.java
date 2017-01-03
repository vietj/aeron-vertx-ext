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

package com.julienviet.rxjava.aeron.client;

import java.util.Map;
import rx.Observable;
import java.util.concurrent.TimeUnit;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link com.julienviet.aeron.client.AeronSubscription original} non RX-ified interface using Vert.x codegen.
 */

public class AeronSubscription implements ReadStream<Buffer> {

  final com.julienviet.aeron.client.AeronSubscription delegate;

  public AeronSubscription(com.julienviet.aeron.client.AeronSubscription delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<Buffer> observable;

  public synchronized rx.Observable<Buffer> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.buffer.Buffer, Buffer> conv = Buffer::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.buffer.Buffer, Buffer> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
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
    ((io.vertx.core.streams.ReadStream) delegate).handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(Buffer.newInstance(event));
      }
    });
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


  public static AeronSubscription newInstance(com.julienviet.aeron.client.AeronSubscription arg) {
    return arg != null ? new AeronSubscription(arg) : null;
  }
}
