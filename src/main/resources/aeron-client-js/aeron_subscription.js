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

/** @module aeron-client-js/aeron_subscription */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var ReadStream = require('vertx-js/read_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAeronSubscription = com.julienviet.aeron.client.AeronSubscription;

/**

 @class
*/
var AeronSubscription = function(j_val) {

  var j_aeronSubscription = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**
   Set the number of buffers polled from the <code>Subscription</code>.

   @public
   @param size {number} the number of buffers 
   @return {AeronSubscription} a reference to this, so the API can be used fluently
   */
  this.batchSize = function(size) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_aeronSubscription["batchSize(int)"](size);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the delay between 2 polls of the <code>Subscription</code>.

   @public
   @param delay {number} the delay between two intervals 
   @param unit {Object} the interval time unit 
   @return {AeronSubscription} a reference to this, so the API can be used fluently
   */
  this.batchInterval = function(delay, unit) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
      j_aeronSubscription["batchInterval(long,java.util.concurrent.TimeUnit)"](delay, java.util.concurrent.TimeUnit.valueOf(unit));
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the subscription.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronSubscription["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AeronSubscription}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronSubscription["exceptionHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AeronSubscription}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronSubscription["handler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {AeronSubscription}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronSubscription["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {AeronSubscription}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronSubscription["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {AeronSubscription}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronSubscription["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_aeronSubscription;
};

// We export the Constructor function
module.exports = AeronSubscription;