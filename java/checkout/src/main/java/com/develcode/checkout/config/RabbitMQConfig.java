package com.develcode.checkout.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.develcode.checkout.bo.CheckoutBO;
import com.develcode.checkout.enums.RabbitMQConstantsEnum;

@Configuration
public class RabbitMQConfig {

    @Bean
    DirectExchange checkoutExchange() {
        return new DirectExchange(RabbitMQConstantsEnum.CHECKOUT_EXCHANGE.getValue());
    }

    @Bean
    Queue orderCheckoutQueue() {
        return new Queue(RabbitMQConstantsEnum.ORDER_CHECKOUT_QUEUE.getValue(), true);
    }

    @Bean
    Binding orderCheckoutBinding() {
        return BindingBuilder.bind(orderCheckoutQueue())
                .to(checkoutExchange())
                .with(RabbitMQConstantsEnum.CHECKOUT_ROUTING_KEY.getValue());
    }

    @Bean
    Queue paymentStatusQueue() {
        return new Queue(RabbitMQConstantsEnum.PAYMENT_STATUS_QUEUE.getValue(), true);
    }

    @Bean
    Binding paymentStatusBinding() {
        return BindingBuilder.bind(paymentStatusQueue())
                .to(checkoutExchange())
                .with(RabbitMQConstantsEnum.PAYMENT_STATUS_ROUTING_KEY.getValue());
    }

    @Bean
    SimpleMessageListenerContainer processedCheckoutListener(ConnectionFactory connectionFactory,
            CheckoutBO listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQConstantsEnum.PAYMENT_STATUS_QUEUE.getValue());
        container.setMessageListener(listenerAdapter);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setConcurrency("4");
        container.setPrefetchCount(20);
        return container;
    }

}
