package com.wolroys.orderservice.service;

import com.paypal.orders.*;
import com.wolroys.shopentity.entity.Order;
import com.wolroys.shopentity.mapper.OrderMapper;
import com.wolroys.shoputils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderRequest createOrderRequest(Long orderId){
        Order order = orderMapper.toOrder(
                orderService.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order wasn't found")));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("My market")
                .landingPage("BILLING")
                .shippingPreference("SET_PROVIDED_ADDRESS");
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .referenceId(orderId.toString())
                .description("My spring-cloud-shop")
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.valueOf(order.getTotalAmount())))
                .items(order.getProducts().stream()
                        .map(product -> new Item()
                                .name(product.getName())
                                .unitAmount(new Money()
                                        .currencyCode("USD")
                                        .value(String.valueOf(product.getPrice())))
                                .quantity(String.valueOf(product.getQuantity())))
                        .toList())
                .shippingDetail(new ShippingDetail()
                        .name(new Name().fullName(String.valueOf("sb-oirkd28492435@business.example.com")))
                        .addressPortable(new AddressPortable()
                                .addressLine1("Baker Street 221B")
                                .addressLine2("floor 4")
                                .adminArea1("CA")
                                .adminArea2("London")
                                .postalCode("347632")
                                .countryCode("US")));

        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);

        return orderRequest;
    }
}
