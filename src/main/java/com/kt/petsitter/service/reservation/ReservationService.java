package com.kt.petsitter.service.reservation;

import com.kt.petsitter.dto.reservation.request.CreateReservationRequest;
import com.kt.petsitter.dto.reservation.response.ReservationResponse;
import com.kt.petsitter.entity.*;
import com.kt.petsitter.repository.pet.PetRepository;
import com.kt.petsitter.repository.petservice.PetServiceRepository;
import com.kt.petsitter.repository.petsitter.PetSitterRepository;
import com.kt.petsitter.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PetSitterRepository petSitterRepository;
    private final PetRepository petRepository;
    private final PetServiceRepository petServiceRepository;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        PetSitter petSitter = petSitterRepository.findById(request.getPetsitterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 펫시터입니다."));

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));

        PetService service = petServiceRepository.findById(request.getPetId())
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 서비스입니다."));

        PetSitterReserve reserve = PetSitterReserve.builder()
                .petSitter(petSitter)
                .pet(pet)
                .petService(service)
                .beginTime(LocalDateTime.parse(request.getStartTime()))
                .endTime(LocalDateTime.parse(request.getEndTime()))
                .price(request.getTotalPrice())
                .isaccept(false) // 초기 상태
                .build();

        PetSitterReserve savedReserve = reservationRepository.save(reserve);
        return ReservationResponse.from(savedReserve);
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByPetUserId(userId).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
