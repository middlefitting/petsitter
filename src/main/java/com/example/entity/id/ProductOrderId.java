package com.example.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderId implements Serializable {
    private Long product;
    private Long order;
}
