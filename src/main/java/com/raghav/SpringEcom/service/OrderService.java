package com.raghav.SpringEcom.service;

import com.raghav.SpringEcom.model.Order;
import com.raghav.SpringEcom.model.OrderItem;
import com.raghav.SpringEcom.model.Product;
import com.raghav.SpringEcom.model.dto.OrderItemRequest;
import com.raghav.SpringEcom.model.dto.OrderItemResponse;
import com.raghav.SpringEcom.model.dto.OrderRequest;
import com.raghav.SpringEcom.model.dto.OrderResponse;
import com.raghav.SpringEcom.repo.OrderRepo;
import com.raghav.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("Placed");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemRequest itemRequest : orderRequest.items()){
            Product product = productRepo.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());

            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);

        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()){
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );

            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
        );

        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {
        return null;
    }
}
