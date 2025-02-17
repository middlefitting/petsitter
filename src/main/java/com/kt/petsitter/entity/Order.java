package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantUid;
    private Long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paytype_id")
    private PayType payType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderstatus_id")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<ProductOrder> productOrders = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private List<PetSitterOrder> petSitterOrders = new ArrayList<>();

    @Builder
    public Order(String merchantUid, Long totalPrice, User user, PayType payType) {
        this.merchantUid = merchantUid;
        this.totalPrice = totalPrice;
        this.user = user;
        this.payType = payType;
    }
}
