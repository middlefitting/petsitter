package com.kt.petsitter.dto.reservation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentInfoRequest {
    private String merchantUid;
    private String payMethod;
}
