package com.kt.petsitter.dto.reservation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResponse {
    private String merchantUid;
    private Long totalPrice;
    private String payMethod;
    private String petName;
    private String serviceName;
}
