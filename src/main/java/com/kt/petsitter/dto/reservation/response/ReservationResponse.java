package com.kt.petsitter.dto.reservation.response;

import com.kt.petsitter.entity.PetSitterReserve;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationResponse {
    private Long id;
    private String petName;
    private String petsitterName;
    private String serviceType;
    private String date;
    private String startTime;
    private String endTime;
    private Long totalPrice;
    private String status;

    public static ReservationResponse from(PetSitterReserve reserve) {
        return ReservationResponse.builder()
                .id(reserve.getId())
                .petName(reserve.getPet().getName())
                .petsitterName(reserve.getPetSitter().getName())
                .serviceType(reserve.getPetService().getServicename())
                // .date(reserve.getDate())
                // .startTime(reserve.getStartTime())
                // .endTime(reserve.getEndTime())
                // .totalPrice(reserve.getTotalPrice())
                // .status(reserve.getStatus())
                .build();
    }
}
