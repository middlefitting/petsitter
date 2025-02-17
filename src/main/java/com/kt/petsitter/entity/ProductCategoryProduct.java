package com.kt.petsitter.entity;

import com.kt.petsitter.entity.id.ProductCategoryProductId;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "productcategory_product")
@IdClass(ProductCategoryProductId.class)
public class ProductCategoryProduct extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2")
    private ProductCategory productCategory;
}
