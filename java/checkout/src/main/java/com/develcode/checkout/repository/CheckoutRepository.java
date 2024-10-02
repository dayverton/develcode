package com.develcode.checkout.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.develcode.checkout.model.OrderModel;

@Repository
public interface CheckoutRepository extends MongoRepository<OrderModel, String> {

}
