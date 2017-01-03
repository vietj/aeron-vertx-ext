require 'vertx/vertx'
require 'aeron-client/aeron_publication'
require 'aeron-client/aeron_subscription'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.aeron.client.AeronClient
module AeronClient
  class AeronClient
    # @private
    # @param j_del [::AeronClient::AeronClient] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::AeronClient::AeronClient] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [Hash] options 
    # @return [::AeronClient::AeronClient]
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::ComJulienvietAeronClient::AeronClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::AeronClient::AeronClient)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::ComJulienvietAeronClient::AeronClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::ComJulienvietAeronClient::AeronClientOptions.java_class]).call(vertx.j_del,Java::ComJulienvietAeronClient::AeronClientOptions.new(::Vertx::Util::Utils.to_json_object(options))),::AeronClient::AeronClient)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,options)"
    end
    # @param [String] channel 
    # @param [Fixnum] streamId 
    # @yield 
    # @return [self]
    def add_publication(channel=nil,streamId=nil)
      if channel.class == String && streamId.class == Fixnum && block_given?
        @j_del.java_method(:addPublication, [Java::java.lang.String.java_class,Java::int.java_class,Java::IoVertxCore::Handler.java_class]).call(channel,streamId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::AeronClient::AeronPublication) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling add_publication(channel,streamId)"
    end
    # @param [String] channel 
    # @param [Fixnum] streamId 
    # @yield 
    # @return [self]
    def add_subscription(channel=nil,streamId=nil)
      if channel.class == String && streamId.class == Fixnum && block_given?
        @j_del.java_method(:addSubscription, [Java::java.lang.String.java_class,Java::int.java_class,Java::IoVertxCore::Handler.java_class]).call(channel,streamId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::AeronClient::AeronSubscription) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling add_subscription(channel,streamId)"
    end
    # @yield 
    # @return [self]
    def exception_handler
      if block_given?
        @j_del.java_method(:exceptionHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.from_throwable(event)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling exception_handler()"
    end
    #  Close the client.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
