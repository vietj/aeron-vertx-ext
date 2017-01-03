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

/** @module aeron-client-js/aeron_client */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var AeronPublication = require('aeron-client-js/aeron_publication');
var AeronSubscription = require('aeron-client-js/aeron_subscription');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAeronClient = com.julienviet.aeron.client.AeronClient;
var AeronClientOptions = com.julienviet.aeron.client.AeronClientOptions;

/**

 @class
*/
var AeronClient = function(j_val) {

  var j_aeronClient = j_val;
  var that = this;

  /**

   @public
   @param channel {string} 
   @param streamId {number} 
   @param pubHandler {function} 
   @return {AeronClient}
   */
  this.addPublication = function(channel, streamId, pubHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_aeronClient["addPublication(java.lang.String,int,io.vertx.core.Handler)"](channel, streamId, function(ar) {
      if (ar.succeeded()) {
        pubHandler(utils.convReturnVertxGen(ar.result(), AeronPublication), null);
      } else {
        pubHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param channel {string} 
   @param streamId {number} 
   @param subHandler {function} 
   @return {AeronClient}
   */
  this.addSubscription = function(channel, streamId, subHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_aeronClient["addSubscription(java.lang.String,int,io.vertx.core.Handler)"](channel, streamId, function(ar) {
      if (ar.succeeded()) {
        subHandler(utils.convReturnVertxGen(ar.result(), AeronSubscription), null);
      } else {
        subHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AeronClient}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_aeronClient["exceptionHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the client.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_aeronClient["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_aeronClient;
};

/**

 @memberof module:aeron-client-js/aeron_client
 @param vertx {Vertx} 
 @param options {Object} 
 @return {AeronClient}
 */
AeronClient.create = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JAeronClient["create(io.vertx.core.Vertx)"](__args[0]._jdel), AeronClient);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JAeronClient["create(io.vertx.core.Vertx,com.julienviet.aeron.client.AeronClientOptions)"](__args[0]._jdel, __args[1] != null ? new AeronClientOptions(new JsonObject(JSON.stringify(__args[1]))) : null), AeronClient);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = AeronClient;