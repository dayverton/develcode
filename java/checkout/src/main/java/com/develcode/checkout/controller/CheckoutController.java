package com.develcode.checkout.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develcode.checkout.bo.CheckoutBO;
import com.develcode.checkout.dto.request.OrderRequestDTO;
import com.develcode.checkout.dto.response.GenericApiResponseDTO;
import com.develcode.checkout.dto.response.OrderResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutBO checkoutBO;

    @GetMapping("/order/all")
    public ResponseEntity<GenericApiResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        GenericApiResponseDTO<List<OrderResponseDTO>> response = new GenericApiResponseDTO<>();

        try {
            List<OrderResponseDTO> ordersList = checkoutBO.getAllOrders();

            response.setSuccess(true);
            response.setData(ordersList);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/send")
    public ResponseEntity<GenericApiResponseDTO<OrderResponseDTO>> saveOrder(
            @RequestBody @Valid OrderRequestDTO order, BindingResult bindingResult) {
        GenericApiResponseDTO<OrderResponseDTO> response = new GenericApiResponseDTO<>();

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            response.setSuccess(false);
            response.setError(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            OrderResponseDTO savedOrder = checkoutBO.saveOrder(order);

            response.setSuccess(true);
            response.setData(savedOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
