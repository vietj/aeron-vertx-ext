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
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.streams.WriteStream;
import io.vertx.core.Handler;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link com.julienviet.aeron.client.AeronPublication original} non RX-ified interface using Vert.x codegen.
 */

public class AeronPublication implements WriteStream<Buffer> {

  final com.julienviet.aeron.client.AeronPublication delegate;

  public AeronPublication(com.julienviet.aeron.client.AeronPublication delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public void end() { 
    delegate.end();
  }

  public void end(Buffer t) { 
    delegate.end((io.vertx.core.buffer.Buffer)t.getDelegate());
  }

  public boolean writeQueueFull() { 
    boolean ret = delegate.writeQueueFull();
    return ret;
  }

  /**
   * Set the number of buffers offered to a Publication.
   * @param size the batch size
   * @return a reference to this, so the API can be used fluently
   */
  public AeronPublication setBatchSize(int size) { 
    delegate.setBatchSize(size);
    return this;
  }

  /**
   * Set the number of retries when a buffer offered to a <code>Publication</code> resulted in a <code>Publication.BACK_PRESSURED</code>
   * or an <code>ADMIN_ACTION</code> result.
   * @param retries the number of retries
   * @return a reference to this, so the API can be used fluently
   */
  public AeronPublication setOfferRetries(int retries) { 
    delegate.setOfferRetries(retries);
    return this;
  }

  /**
   * Set the delay to reschedule a publiation batch of buffers offered to a <code>Publication.Publication</code> when
   * the result is <code>Publication.BACK_PRESSURED</code> or <code>ADMIN_ACTION</code> and the number of retries has been exceeded.
   * @param delay the delay in milliseconds
   * @return a reference to this, so the API can be used fluently
   */
  public AeronPublication setOfferRetryDelay(int delay) { 
    delegate.setOfferRetryDelay(delay);
    return this;
  }

  /**
   * Set the delay to reschedule a publication batch when the result is <code>Publication.NOT_CONNECTED</code>.
   * @param delay the delay in milliseconds
   * @return a reference to this, so the API can be used fluently
   */
  public AeronPublication setConnectRetryDelay(int delay) { 
    delegate.setConnectRetryDelay(delay);
    return this;
  }

  /**
   * Close the publication.
   */
  public void close() { 
    delegate.close();
  }

  public AeronPublication exceptionHandler(Handler<Throwable> handler) { 
    ((io.vertx.core.streams.WriteStream) delegate).exceptionHandler(handler);
    return this;
  }

  public AeronPublication write(Buffer buffer) { 
    ((io.vertx.core.streams.WriteStream) delegate).write((io.vertx.core.buffer.Buffer)buffer.getDelegate());
    return this;
  }

  public AeronPublication setWriteQueueMaxSize(int i) { 
    ((io.vertx.core.streams.WriteStream) delegate).setWriteQueueMaxSize(i);
    return this;
  }

  public AeronPublication drainHandler(Handler<Void> handler) { 
    ((io.vertx.core.streams.WriteStream) delegate).drainHandler(handler);
    return this;
  }


  public static AeronPublication newInstance(com.julienviet.aeron.client.AeronPublication arg) {
    return arg != null ? new AeronPublication(arg) : null;
  }
}
