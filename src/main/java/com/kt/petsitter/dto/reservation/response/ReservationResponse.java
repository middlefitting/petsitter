package com.kt.petsitter.dto.reservation.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate date;
    private String startTime;
    private String endTime;
    private Long totalPrice;
    private Boolean status;
    private Boolean isPaid;

    public static ReservationResponse from(PetSitterReserve reserve) {
        return ReservationResponse.builder()
                .id(reserve.getId())
                .petName(reserve.getPet().getName())
                .petsitterName(reserve.getPetSitter().getName())
                .serviceType(reserve.getPetService().getServicename())
                .date(LocalDate.from(reserve.getBeginTime()))
                .startTime(String.valueOf(reserve.getBeginTime()))
                .endTime(String.valueOf(reserve.getEndTime()))
                .totalPrice(reserve.getPrice())
                .status(reserve.getIsaccept())
                .isPaid(reserve.getIspaied())
                .build();
    }
}
