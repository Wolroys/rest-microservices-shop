package com.wolroys.orderservice.controller;

import com.wolroys.orderservice.service.OrderService;
import com.wolroys.shopentity.dto.OrderDto;
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
    public ResponseEntity<List<OrderDto>> getAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long id){
        return orderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can't find anything"));
    }

    @GetMapping("/user/")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getUserOrders(@RequestHeader String username){
        return orderService.getAllByUsername(username);
    }

    @GetMapping("/complete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void completeOrder(@PathVariable Long id){
        orderService.completeOrder(id);
    }

    @GetMapping("/cancel/{id}")
    public void cancelOrder(@PathVariable Long id){
        if (!orderService.cancelOrder(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
