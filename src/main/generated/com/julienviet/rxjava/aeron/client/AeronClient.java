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
import io.vertx.rxjava.core.Vertx;
import com.julienviet.aeron.client.AeronClientOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link com.julienviet.aeron.client.AeronClient original} non RX-ified interface using Vert.x codegen.
 */

public class AeronClient {

  final com.julienviet.aeron.client.AeronClient delegate;

  public AeronClient(com.julienviet.aeron.client.AeronClient delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static AeronClient create(Vertx vertx, AeronClientOptions options) { 
    AeronClient ret = AeronClient.newInstance(com.julienviet.aeron.client.AeronClient.create((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  public static AeronClient create(Vertx vertx) { 
    AeronClient ret = AeronClient.newInstance(com.julienviet.aeron.client.AeronClient.create((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  public AeronClient addPublication(String channel, int streamId, Handler<AsyncResult<AeronPublication>> pubHandler) { 
    delegate.addPublication(channel, streamId, new Handler<AsyncResult<com.julienviet.aeron.client.AeronPublication>>() {
      public void handle(AsyncResult<com.julienviet.aeron.client.AeronPublication> ar) {
        if (ar.succeeded()) {
          pubHandler.handle(io.vertx.core.Future.succeededFuture(AeronPublication.newInstance(ar.result())));
        } else {
          pubHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
    return this;
  }

  public Observable<AeronPublication> addPublicationObservable(String channel, int streamId) { 
    io.vertx.rx.java.ObservableFuture<AeronPublication> pubHandler = io.vertx.rx.java.RxHelper.observableFuture();
    addPublication(channel, streamId, pubHandler.toHandler());
    return pubHandler;
  }

  public AeronClient addSubscription(String channel, int streamId, Handler<AsyncResult<AeronSubscription>> subHandler) { 
    delegate.addSubscription(channel, streamId, new Handler<AsyncResult<com.julienviet.aeron.client.AeronSubscription>>() {
      public void handle(AsyncResult<com.julienviet.aeron.client.AeronSubscription> ar) {
        if (ar.succeeded()) {
          subHandler.handle(io.vertx.core.Future.succeededFuture(AeronSubscription.newInstance(ar.result())));
        } else {
          subHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
    return this;
  }

  public Observable<AeronSubscription> addSubscriptionObservable(String channel, int streamId) { 
    io.vertx.rx.java.ObservableFuture<AeronSubscription> subHandler = io.vertx.rx.java.RxHelper.observableFuture();
    addSubscription(channel, streamId, subHandler.toHandler());
    return subHandler;
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


  public static AeronClient newInstance(com.julienviet.aeron.client.AeronClient arg) {
    return arg != null ? new AeronClient(arg) : null;
  }
}
