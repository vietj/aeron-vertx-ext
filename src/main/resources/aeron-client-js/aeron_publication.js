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

/** @module aeron-client-js/aeron_publication */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var WriteStream = require('vertx-js/write_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAeronPublication = com.julienviet.aeron.client.AeronPublication;

/**

 @class
*/
var AeronPublication = function(j_val) {

  var j_aeronPublication = j_val;
  var that = this;
  WriteStream.call(this, j_val);

  /**

   @public
   @param t {Buffer} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronPublication["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_aeronPublication["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean}
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_aeronPublication["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the number of buffers offered to a Publication.

   @public
   @param size {number} the batch size 
   @return {AeronPublication} a reference to this, so the API can be used fluently
   */
  this.setBatchSize = function(size) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronPublication["setBatchSize(int)"](size);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the number of retries when a buffer offered to a <code>Publication</code> resulted in a <code>Publication.BACK_PRESSURED</code>
   or an <code>ADMIN_ACTION</code> result.

   @public
   @param retries {number} the number of retries 
   @return {AeronPublication} a reference to this, so the API can be used fluently
   */
  this.setOfferRetries = function(retries) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronPublication["setOfferRetries(int)"](retries);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the delay to reschedule a publiation batch of buffers offered to a <code>Publication.Publication</code> when
   the result is <code>Publication.BACK_PRESSURED</code> or <code>ADMIN_ACTION</code> and the number of retries has been exceeded.

   @public
   @param delay {number} the delay in milliseconds 
   @return {AeronPublication} a reference to this, so the API can be used fluently
   */
  this.setOfferRetryDelay = function(delay) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronPublication["setOfferRetryDelay(int)"](delay);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the delay to reschedule a publication batch when the result is <code>Publication.NOT_CONNECTED</code>.

   @public
   @param delay {number} the delay in milliseconds 
   @return {AeronPublication} a reference to this, so the API can be used fluently
   */
  this.setConnectRetryDelay = function(delay) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronPublication["setConnectRetryDelay(int)"](delay);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the publication.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronPublication["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AeronPublication}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronPublication["exceptionHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param buffer {Buffer} 
   @return {AeronPublication}
   */
  this.write = function(buffer) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_aeronPublication["write(io.vertx.core.buffer.Buffer)"](buffer._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param i {number} 
   @return {AeronPublication}
   */
  this.setWriteQueueMaxSize = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronPublication["setWriteQueueMaxSize(int)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AeronPublication}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronPublication["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_aeronPublication;
};

// We export the Constructor function
module.exports = AeronPublication;