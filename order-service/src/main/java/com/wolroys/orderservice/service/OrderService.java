package com.wolroys.orderservice.service;

import com.wolroys.orderservice.repository.OrderRepository;
import com.wolroys.shopentity.entity.Order;
import com.wolroys.shopentity.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public List<Order> getAllForUser(Long userId){
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    public void completeOrder(Long id){
        Order order = findById(id)
                .orElseThrow(NoSuchElementException::new);

        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);
    }

}
