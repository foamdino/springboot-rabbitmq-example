package com.thg.msging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    private final RabbitTemplate rabbitTemplate;
    private final Config config;

    @Autowired
    public Producer(RabbitTemplate rabbitTemplate, Config config) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
    }

    public void send(String msg) {
        logger.info("Sending message");
        rabbitTemplate.convertAndSend(config.exchange, "com.thg.msging", msg);
    }
}
