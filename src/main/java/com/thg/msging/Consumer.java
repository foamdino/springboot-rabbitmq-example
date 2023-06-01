package com.thg.msging;

import com.rabbitmq.client.Channel;
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

    @Autowired
    public Consumer(Config config) {
        this.config = config;
    }

    @RabbitListener(queues = { "${q}" })
    public void consume(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        if (config.consume) {
            try {
                logger.info("Received <" + message + ">");
                channel.basicAck(tag, false);
                latch.countDown();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }
}
