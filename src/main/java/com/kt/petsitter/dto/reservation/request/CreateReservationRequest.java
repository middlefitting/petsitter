package com.kt.petsitter.dto.reservation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReservationRequest {
    private Long petsitterId;
    private Long petId;
    private String serviceId;
    private String date;
    private String startTime;
    private String endTime;
    private Long totalPrice;
}
