package com.raghav.SpringEcom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OrderItem {
    @Id
    private Long id;
    @ManyToOne
    private Product product;
    private int quantity;
    private BigDecimal totalPrice;
    @ManyToOne(fetch= FetchType.LAZY)
    private Order order;

}
