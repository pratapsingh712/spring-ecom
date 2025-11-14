package com.raghav.SpringEcom.controller;

import com.raghav.SpringEcom.model.dto.OrderRequest;
import com.raghav.SpringEcom.model.dto.OrderResponse;
import com.raghav.SpringEcom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrder(){
        List<OrderResponse> responses = orderService.getAllOrderResponses();
        return new ResponseEntity<>(responses,HttpStatus.OK);
    }
}
