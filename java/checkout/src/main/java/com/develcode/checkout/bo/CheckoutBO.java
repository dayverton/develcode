package com.develcode.checkout.bo;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.develcode.checkout.dto.request.OrderEventRequestDTO;
import com.develcode.checkout.dto.request.OrderRequestDTO;
import com.develcode.checkout.dto.response.OrderResponseDTO;
import com.develcode.checkout.dto.response.PaymentConfirmationResponseDTO;
import com.develcode.checkout.enums.CheckoutStatusEnum;
import com.develcode.checkout.enums.RabbitMQConstantsEnum;
import com.develcode.checkout.model.OrderModel;
import com.develcode.checkout.service.CheckoutService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import jakarta.annotation.Nullable;

@Service
public class CheckoutBO implements ChannelAwareMessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public List<OrderResponseDTO> getAllOrders() {
        List<OrderModel> orderList = checkoutService.findAllOrders();

        return orderList.stream()
                .map(model -> {
                    OrderResponseDTO dto = modelMapper.map(model, OrderResponseDTO.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderResponseDTO saveOrder(OrderRequestDTO postCommentDTO) throws JsonProcessingException {
        OrderModel savedOrder = checkoutService.saveOrder(postCommentDTO);

        if (savedOrder != null && savedOrder.getId() != null) {
            OrderEventRequestDTO orderEvent = OrderEventRequestDTO
                    .builder()
                    .id(savedOrder.getId())
                    .amount(savedOrder.getAmount())
                    .build();

            String orderContentJson = objectMapper.writeValueAsString(orderEvent);

            sendOrderEvent(orderContentJson);
        }

        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }

    private void sendOrderEvent(String orderContentJson) {
        rabbitTemplate.convertAndSend(RabbitMQConstantsEnum.CHECKOUT_EXCHANGE.getValue(),
                RabbitMQConstantsEnum.CHECKOUT_ROUTING_KEY.getValue(), orderContentJson);
    }

    @SuppressWarnings("null")
    @Override
    public void onMessage(Message message, @Nullable Channel channel) throws Exception {

        PaymentConfirmationResponseDTO paymentStatusMessage = objectMapper.readValue(message.getBody(),
                PaymentConfirmationResponseDTO.class);

        String paymentStatus = paymentStatusMessage.isSuccess()
                ? CheckoutStatusEnum.PAID.getValue()
                : CheckoutStatusEnum.CANCELLED.getValue();

        if (channel != null && channel.isOpen()) {
            OrderModel updatedOrderModel = checkoutService.updateOrder(paymentStatusMessage.getOrderId(),
                    paymentStatus);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            System.out.println(String.format(
                    "Pedido %s %s: ", updatedOrderModel.getId(), updatedOrderModel.getStatus()));

        } else {
            throw new Exception("Canal fechado");
        }

    }

}
