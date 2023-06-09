package com.thg.msging;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    public String qName;
    public String exchange;
    public int messages;
    public boolean produce;
    public boolean consume;
    private String host;
    private String username;
    private String password;

    public Config(@Value("${q}") String qName,
                  @Value("${exchange}") String exchange,
                  @Value("${messages}") int messages,
                  @Value("${produce}") boolean produce,
                  @Value("${consume}") boolean consume,
                  @Value("${spring.rabbitmq.host}") String host,
                  @Value("${spring.rabbitmq.username}") String username,
                  @Value("${spring.rabbitmq.password}") String password) {
        this.qName = qName;
        this.exchange = exchange;
        this.messages = messages;
        this.produce = produce;
        this.consume = consume;
        this.host = host;
        this.username = username;
        this.password = password;
    }

//    @Bean
//    public MeterRegistry getMeterRegistry() {
//        CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();
//        return meterRegistry;
//    }

    @Bean
    public Queue queue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "quorum");
        return new Queue(qName, true, false, false, args);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("com.thg.#");
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        return connectionFactory;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(qName);
//        container.setExposeListenerChannel(true);
//        // set ack to manual to ensure we do actually ack messages
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(new MessageListenerAdapter(new Consumer()));
//
//        return container;
//    }
}
