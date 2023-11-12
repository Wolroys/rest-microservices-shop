package com.wolroys.orderservice.service;

import com.wolroys.orderservice.repository.OrderRepository;
import com.wolroys.shopentity.dto.OrderDto;
import com.wolroys.shopentity.entity.Order;
import com.wolroys.shopentity.entity.Status;
import com.wolroys.shopentity.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;

    public List<OrderDto> findAll(){
        return orderRepository
                .findAll()
                .stream().map(orderMapper::toOrderDto)
                .toList();
    }

    public Optional<OrderDto> findById(Long id){
        return orderRepository.findById(id)
                .map(orderMapper::toOrderDto);
    }

    public List<OrderDto> getAllForUser(Long userId){
        return orderRepository.findAllByUserId(userId)
                .stream().
                map(orderMapper::toOrderDto)
                .toList();
    }

    @Transactional
    public void completeOrder(Long id){
        OrderDto dto = findById(id)
                .orElseThrow(NoSuchElementException::new);

        Order order = orderMapper.toOrder(dto);

        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public void createOrder(OrderDto orderDto){
        Order order = orderMapper.toOrder(orderDto);

        orderRepository.save(order);
    }

    @Transactional
    public boolean cancelOrder(Long id){
        return orderRepository.findById(id)
                .map(entity -> {
                    entity.setStatus(Status.CANCELLED);
                    orderRepository.saveAndFlush(entity);
                    return true;
                }).orElse(false);
        //TODO
    }


    //TODO create notification method about creating order

}
