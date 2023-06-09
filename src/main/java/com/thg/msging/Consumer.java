package com.thg.msging;

import com.rabbitmq.client.Channel;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Consumer  {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final Config config;

    private Counter consumeMsg;
    private Counter consumeMsgExceptions;

    @Autowired
    public Consumer(Config config, MeterRegistry meterRegistry) {
        this.config = config;
        consumeMsg = meterRegistry.counter("consumeMsg.success");
        consumeMsgExceptions = meterRegistry.counter("consumeMsg.exceptions");
    }

    @RabbitListener(queues = { "${q}" })
    public void consume(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        if (config.consume) {
            try {
                logger.info("Received <" + message + ">");
                channel.basicAck(tag, false);
                latch.countDown();
                consumeMsg.increment();
            } catch (Exception e) {
                logger.info(e.getMessage());
                consumeMsgExceptions.increment();
            }
        }
    }
}
