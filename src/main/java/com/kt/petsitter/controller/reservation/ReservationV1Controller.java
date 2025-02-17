package com.kt.petsitter.controller.reservation;

import com.kt.petsitter.dto.reservation.request.CreateReservationRequest;
import com.kt.petsitter.dto.reservation.request.PaymentInfoRequest;
import com.kt.petsitter.dto.reservation.response.ReservationResponse;
import com.kt.petsitter.dto.reservation.response.OrderDetailResponse;
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

    @GetMapping("/petsitter/users/{userId}")
    public ResponseEntity<RestResponse<List<ReservationResponse>>> getPetSitterReservations(
            @PathVariable Long userId) {
        List<ReservationResponse> reservations = reservationService.getPetSitterReservations(userId);
        return ResponseEntity.ok(RestResponse.success(reservations, "펫시터 예약 목록 조회 성공"));
    }

    @PostMapping("/{reservationId}/accept")
    public ResponseEntity<RestResponse<ReservationResponse>> acceptReservation(
            @PathVariable Long reservationId) {
        ReservationResponse response = reservationService.acceptReservation(reservationId);
        return ResponseEntity.ok(RestResponse.success(response, "예약이 승인되었습니다."));
    }

    @PostMapping("/{reservationId}/reject")
    public ResponseEntity<RestResponse<ReservationResponse>> rejectReservation(
            @PathVariable Long reservationId) {
        ReservationResponse response = reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok(RestResponse.success(response, "예약이 거절되었습니다."));
    }

    @PostMapping("/{reservationId}/pay")
    public ResponseEntity<RestResponse<Void>> updatePaymentInfo(
            @PathVariable Long reservationId,
            @RequestBody PaymentInfoRequest paymentInfoRequest) {
        reservationService.updatePaymentInfo(reservationId, paymentInfoRequest.getMerchantUid(), paymentInfoRequest.getPayMethod());
        return ResponseEntity.ok(RestResponse.success(null, "결제 정보가 업데이트되었습니다."));
    }

    @GetMapping("/{reservationId}/order-details")
    public ResponseEntity<RestResponse<OrderDetailResponse>> getOrderDetails(@PathVariable Long reservationId) {
        OrderDetailResponse orderDetails = reservationService.getOrderDetails(reservationId);
        return ResponseEntity.ok(RestResponse.success(orderDetails, "결제 내역 조회 성공"));
    }
}
