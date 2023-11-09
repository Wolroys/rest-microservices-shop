package com.wolroys.orderservice.controller;

import com.wolroys.orderservice.service.OrderService;
import com.wolroys.shopentity.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order getOrderById(@PathVariable Long id){
        return orderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can't find anything"));
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getUserOrders(@PathVariable Long userId){ //TODO auth
        return orderService.getAllForUser(userId);
    }

    @GetMapping("/complete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void completeOrder(@PathVariable Long id){
        orderService.completeOrder(id);
    }
}
