package com.kt.petsitter.service.reservation;

import com.kt.petsitter.dto.reservation.request.CreateReservationRequest;
import com.kt.petsitter.dto.reservation.response.ReservationResponse;
import com.kt.petsitter.dto.reservation.response.OrderDetailResponse;
import com.kt.petsitter.entity.*;
import com.kt.petsitter.repository.order.OrderRepository;
import com.kt.petsitter.repository.paytype.PayTypeRepository;
import com.kt.petsitter.repository.pet.PetRepository;
import com.kt.petsitter.repository.petservice.PetServiceRepository;
import com.kt.petsitter.repository.petsitter.PetSitterRepository;
import com.kt.petsitter.repository.petsitterorder.PetSitterOrderRepository;
import com.kt.petsitter.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PetSitterRepository petSitterRepository;
    private final PetRepository petRepository;
    private final PetServiceRepository petServiceRepository;
    private final PayTypeRepository payTypeRepository;
    private final OrderRepository orderRepository;
    private final PetSitterOrderRepository petSitterOrderRepository;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        PetSitter petSitter = petSitterRepository.findById(request.getPetsitterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 펫시터입니다."));

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));

        PetService service = petServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 서비스입니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTime = LocalDateTime.parse(request.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(request.getEndTime(), formatter);

        PetSitterReserve reserve = PetSitterReserve.builder()
                .petSitter(petSitter)
                .pet(pet)
                .petService(service)
                .beginTime(beginTime)
                .endTime(endTime)
                .price(request.getTotalPrice())
                .isaccept(false)
                .ispaied(false)
                .build();

        PetSitterReserve savedReserve = reservationRepository.save(reserve);
        return ReservationResponse.from(savedReserve);
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByPetUserId(userId).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getPetSitterReservations(Long userId) {
        PetSitter petSitter = petSitterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 펫시터 정보를 찾을 수 없습니다."));

        return reservationRepository.findByPetSitterId(petSitter.getId()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse acceptReservation(Long reservationId) {
        PetSitterReserve reserve = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reserve.updateStatus(true); // 예약 상태를 승인으로 변경
        return ReservationResponse.from(reserve);
    }

    @Transactional
    public ReservationResponse rejectReservation(Long reservationId) {
        PetSitterReserve reserve = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.delete(reserve); // 예약 거절 시 삭제
        return ReservationResponse.from(reserve);
    }

    @Transactional
    public void updatePaymentInfo(Long reservationId, String merchantUid, String payMethod) {
        PetSitterReserve reserve = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reserve.getIsaccept()) {
            throw new IllegalStateException("승인되지 않은 예약은 결제할 수 없습니다.");
        }

        if (reserve.getIspaied()) {
            throw new IllegalStateException("이미 결제가 완료된 예약입니다.");
        }

        // PayType 조회
        PayType payType = payTypeRepository.findByType(payMethod)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 결제 유형입니다."));

        // Order 생성
        Order order = Order.builder()
                .merchantUid(merchantUid)
                .totalPrice(reserve.getPrice())
                .user(reserve.getUser())
                .payType(payType)
                .build();
        orderRepository.save(order);

        // PetSitterOrder 생성
        PetSitterOrder petSitterOrder = PetSitterOrder.builder()
                .price(reserve.getPrice())
                .order(order)
                .reserve(reserve)
                .build();
        petSitterOrderRepository.save(petSitterOrder);

        reserve.updatePaymentStatus(true);
        reservationRepository.save(reserve);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetails(Long reservationId) {
        PetSitterOrder petSitterOrder = petSitterOrderRepository.findByReserveId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역을 찾을 수 없습니다."));

        Order order = petSitterOrder.getOrder();
        PetSitterReserve reserve = petSitterOrder.getReserve();

        return OrderDetailResponse.builder()
                .merchantUid(order.getMerchantUid())
                .totalPrice(order.getTotalPrice())
                .payMethod(order.getPayType().getName())
                .petName(reserve.getPet().getName())
                .serviceName(reserve.getPetService().getServicename())
                .build();
    }
}
