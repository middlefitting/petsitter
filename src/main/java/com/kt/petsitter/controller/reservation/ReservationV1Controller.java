package com.kt.petsitter.controller.reservation;

import com.kt.petsitter.dto.reservation.request.CreateReservationRequest;
import com.kt.petsitter.dto.reservation.response.ReservationResponse;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reservations")
@RequiredArgsConstructor
public class ReservationV1Controller {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<RestResponse<ReservationResponse>> createReservation(
            @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(RestResponse.success(response, "예약이 완료되었습니다."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<List<ReservationResponse>>> getUserReservations(
            @PathVariable Long userId) {
        List<ReservationResponse> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(RestResponse.success(reservations, "예약 목록 조회 성공"));
    }
}
