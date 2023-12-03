package com.wolroys.orderservice.controller;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrderCaptureRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.wolroys.orderservice.service.OrderService;
import com.wolroys.orderservice.service.PayPalService;
import com.wolroys.shopentity.entity.Status;
import com.wolroys.shopentity.mapper.OrderMapper;
import com.wolroys.shoputils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/order/paypal")
@RequiredArgsConstructor
public class PayPalController {

    private final PayPalHttpClient payPalHttpClient;
    private final OrderService orderService;
    private final PayPalService payPalService;
    private final OrderMapper orderMapper;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createOrder(@PathVariable Long orderId) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(payPalService.createOrderRequest(orderId));
        HttpResponse<Order> response = payPalHttpClient.execute(request);
        String approveUrl = "https://www.sandbox.paypal.com/checkoutnow?token=" + response.result().id();
        return new ResponseEntity<>(approveUrl, HttpStatus.valueOf(response.statusCode()));
    }

    @PostMapping("/capture/{payPalId}")
    public ResponseEntity<?> captureOrder(@PathVariable String payPalId) throws IOException{
        OrdersCaptureRequest request = new OrdersCaptureRequest(payPalId);
        request.requestBody(new OrderCaptureRequest());

        HttpResponse<Order> response = payPalHttpClient.execute(request);
        Order payPalOrder = response.result();
        if ("COMPLETED".equals(payPalOrder.status())){
            long orderId = Long.parseLong(payPalOrder.purchaseUnits().get(0).referenceId());
            com.wolroys.shopentity.entity.Order order = orderMapper.toOrder(
                    orderService.findById(orderId)
                            .orElseThrow(() -> new ResourceNotFoundException("Order: " + orderId + " wasn't found")));
            order.setStatus(Status.PAID);

            return new ResponseEntity<>("Order completed", HttpStatus.valueOf(response.statusCode()));
        }

        if ("APPROVED".equals(payPalOrder.status())){
            long orderId = Long.parseLong(payPalOrder.purchaseUnits().get(0).referenceId());
            com.wolroys.shopentity.entity.Order order = orderMapper.toOrder(orderService.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order: " + orderId + " wasn't found")));
            order.setStatus(Status.CANCELLED);

            return new ResponseEntity<>("Order cancelled", HttpStatus.valueOf(response.statusCode()));
        }

        return new ResponseEntity<>(payPalOrder, HttpStatus.valueOf(response.statusCode()));
    }
}
