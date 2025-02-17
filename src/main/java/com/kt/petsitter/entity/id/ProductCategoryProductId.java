package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryProductId implements Serializable {
    private Long product;
    private Long productCategory;
}
