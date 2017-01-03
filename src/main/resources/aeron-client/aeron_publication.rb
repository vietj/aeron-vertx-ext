require 'vertx/buffer'
require 'vertx/write_stream'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.aeron.client.AeronPublication
module AeronClient
  class AeronPublication
    include ::Vertx::WriteStream
    # @private
    # @param j_del [::AeronClient::AeronPublication] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::AeronClient::AeronPublication] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Buffer] t 
    # @return [void]
    def end(t=nil)
      if !block_given? && t == nil
        return @j_del.java_method(:end, []).call()
      elsif t.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:end, [Java::IoVertxCoreBuffer::Buffer.java_class]).call(t.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling end(t)"
    end
    # @return [true,false]
    def write_queue_full?
      if !block_given?
        return @j_del.java_method(:writeQueueFull, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling write_queue_full?()"
    end
    #  Set the number of buffers offered to a Publication.
    # @param [Fixnum] size the batch size
    # @return [self]
    def set_batch_size(size=nil)
      if size.class == Fixnum && !block_given?
        @j_del.java_method(:setBatchSize, [Java::int.java_class]).call(size)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_batch_size(size)"
    end
    #  Set the number of retries when a buffer offered to a <code>Publication</code> resulted in a <code>Publication.BACK_PRESSURED</code>
    #  or an <code>ADMIN_ACTION</code> result.
    # @param [Fixnum] retries the number of retries
    # @return [self]
    def set_offer_retries(retries=nil)
      if retries.class == Fixnum && !block_given?
        @j_del.java_method(:setOfferRetries, [Java::int.java_class]).call(retries)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_offer_retries(retries)"
    end
    #  Set the delay to reschedule a publiation batch of buffers offered to a <code>Publication.Publication</code> when
    #  the result is <code>Publication.BACK_PRESSURED</code> or <code>ADMIN_ACTION</code> and the number of retries has been exceeded.
    # @param [Fixnum] delay the delay in milliseconds
    # @return [self]
    def set_offer_retry_delay(delay=nil)
      if delay.class == Fixnum && !block_given?
        @j_del.java_method(:setOfferRetryDelay, [Java::int.java_class]).call(delay)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_offer_retry_delay(delay)"
    end
    #  Set the delay to reschedule a publication batch when the result is <code>Publication.NOT_CONNECTED</code>.
    # @param [Fixnum] delay the delay in milliseconds
    # @return [self]
    def set_connect_retry_delay(delay=nil)
      if delay.class == Fixnum && !block_given?
        @j_del.java_method(:setConnectRetryDelay, [Java::int.java_class]).call(delay)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_connect_retry_delay(delay)"
    end
    #  Close the publication.
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
    # @param [::Vertx::Buffer] buffer 
    # @return [self]
    def write(buffer=nil)
      if buffer.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:write, [Java::IoVertxCoreBuffer::Buffer.java_class]).call(buffer.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(buffer)"
    end
    # @param [Fixnum] i 
    # @return [self]
    def set_write_queue_max_size(i=nil)
      if i.class == Fixnum && !block_given?
        @j_del.java_method(:setWriteQueueMaxSize, [Java::int.java_class]).call(i)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_write_queue_max_size(i)"
    end
    # @yield 
    # @return [self]
    def drain_handler
      if block_given?
        @j_del.java_method(:drainHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling drain_handler()"
    end
  end
end
