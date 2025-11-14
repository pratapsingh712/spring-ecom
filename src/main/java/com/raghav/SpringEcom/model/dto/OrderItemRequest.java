package com.raghav.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) { }
