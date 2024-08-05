package com.cod.AniBirth.order.service;

import com.cod.AniBirth.order.entity.Order;
import com.cod.AniBirth.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }
}
