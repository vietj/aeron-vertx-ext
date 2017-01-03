require 'vertx/buffer'
require 'vertx/read_stream'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.aeron.client.AeronSubscription
module AeronClient
  class AeronSubscription
    include ::Vertx::ReadStream
    # @private
    # @param j_del [::AeronClient::AeronSubscription] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::AeronClient::AeronSubscription] the underlying java delegate
    def j_del
      @j_del
    end
    #  Set the number of buffers polled from the <code>Subscription</code>.
    # @param [Fixnum] size the number of buffers
    # @return [self]
    def batch_size(size=nil)
      if size.class == Fixnum && !block_given?
        @j_del.java_method(:batchSize, [Java::int.java_class]).call(size)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling batch_size(size)"
    end
    #  Set the delay between 2 polls of the <code>Subscription</code>.
    # @param [Fixnum] delay the delay between two intervals
    # @param [:NANOSECONDS,:MICROSECONDS,:MILLISECONDS,:SECONDS,:MINUTES,:HOURS,:DAYS] unit the interval time unit
    # @return [self]
    def batch_interval(delay=nil,unit=nil)
      if delay.class == Fixnum && unit.class == Symbol && !block_given?
        @j_del.java_method(:batchInterval, [Java::long.java_class,Java::JavaUtilConcurrent::TimeUnit.java_class]).call(delay,Java::JavaUtilConcurrent::TimeUnit.valueOf(unit))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling batch_interval(delay,unit)"
    end
    #  Close the subscription.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
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
    # @yield 
    # @return [self]
    def handler
      if block_given?
        @j_del.java_method(:handler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::Vertx::Buffer)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
    # @return [self]
    def pause
      if !block_given?
        @j_del.java_method(:pause, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling pause()"
    end
    # @return [self]
    def resume
      if !block_given?
        @j_del.java_method(:resume, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resume()"
    end
    # @yield 
    # @return [self]
    def end_handler
      if block_given?
        @j_del.java_method(:endHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling end_handler()"
    end
  end
end
