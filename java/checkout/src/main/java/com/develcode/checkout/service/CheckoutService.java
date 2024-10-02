package com.develcode.checkout.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.develcode.checkout.dto.request.OrderRequestDTO;
import com.develcode.checkout.enums.CheckoutStatusEnum;
import com.develcode.checkout.model.OrderModel;
import com.develcode.checkout.repository.CheckoutRepository;

@Service
public class CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    public List<OrderModel> findAllOrders() {
        return checkoutRepository.findAll();
    }

    public OrderModel saveOrder(OrderRequestDTO orderDTO) {

        OrderModel orderModel = new OrderModel();

        BeanUtils.copyProperties(orderDTO, orderModel);

        orderModel.setStatus(CheckoutStatusEnum.AWAITING.getValue());

        return checkoutRepository.save(orderModel);
    }

    public OrderModel getOrderById(String id) throws NotFoundException {
        return checkoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    public OrderModel updateOrder(String id, String status) throws NotFoundException {
        OrderModel orderModel = getOrderById(id);
        orderModel.setStatus(status);

        return checkoutRepository.save(orderModel);
    }

}
