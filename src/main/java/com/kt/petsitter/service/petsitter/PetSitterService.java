package com.kt.petsitter.service.petsitter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.petsitter.dto.petsitter.request.CreatePetSitterRequest;
import com.kt.petsitter.dto.petsitter.response.PetSitterResponse;
import com.kt.petsitter.entity.Address;
import com.kt.petsitter.entity.PetCareTime;
import com.kt.petsitter.entity.PetGroupType;
import com.kt.petsitter.entity.PetService;
import com.kt.petsitter.entity.PetSitter;
import com.kt.petsitter.entity.PetSitterPetGroupType;
import com.kt.petsitter.entity.PetSitterPetService;
import com.kt.petsitter.entity.PetSitterPetSize;
import com.kt.petsitter.entity.PetSize;
import com.kt.petsitter.entity.User;
import com.kt.petsitter.repository.address.AddressRepository;
import com.kt.petsitter.repository.petcaretime.PetCareTimeRepository;
import com.kt.petsitter.repository.petgrouptype.PetGroupTypeRepository;
import com.kt.petsitter.repository.petservice.PetServiceRepository;
import com.kt.petsitter.repository.petsitter.PetSitterRepository;
import com.kt.petsitter.repository.petsitterpetgrouptype.PetSitterPetGroupTypeRepository;
import com.kt.petsitter.repository.petsitterpetservice.PetSitterPetServiceRepository;
import com.kt.petsitter.repository.petsitterpetsize.PetSitterPetSizeRepository;
import com.kt.petsitter.repository.petsize.PetSizeRepository;
import com.kt.petsitter.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetSitterService {
    private final PetSitterRepository petSitterRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PetGroupTypeRepository petGroupTypeRepository;
    private final PetSizeRepository petSizeRepository;
    private final PetServiceRepository petServiceRepository;
    private final PetSitterPetGroupTypeRepository petSitterPetGroupTypeRepository;
    private final PetSitterPetSizeRepository petSitterPetSizeRepository;
    private final PetSitterPetServiceRepository petSitterPetServiceRepository;
    private final PetCareTimeRepository petCareTimeRepository;

    @Transactional
    public PetSitterResponse createPetSitter(CreatePetSitterRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Address address = addressRepository.findById(request.getAddressId())
            .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));

        PetSitter petSitter = PetSitter.builder()
            .name(user.getName())
            .mobile(user.getPhone())
            .address(address)
            .user(user)
            .isaccept(false)
            .build();

        petSitter = petSitterRepository.save(petSitter);

        // 2. 돌봄 가능 품종 정보 추가
        for (CreatePetSitterRequest.PetGroupTypeRequest typeRequest : request.getPetGroupTypes()) {
            PetGroupType petGroupType = petGroupTypeRepository.findById(typeRequest.getPetGroupTypeId())
                .orElseThrow(() -> new IllegalArgumentException("품종 정보를 찾을 수 없습니다."));

            PetSitterPetGroupType petSitterPetGroupType = PetSitterPetGroupType.builder()
                .petSitter(petSitter)
                .petGroupType(petGroupType)
                .build();

            petSitterPetGroupTypeRepository.save(petSitterPetGroupType);
        }

        // 3. 돌봄 가능 크기 정보 추가
        for (Long sizeId : request.getPetSizes()) {
            PetSize petSize = petSizeRepository.findById(sizeId)
                .orElseThrow(() -> new IllegalArgumentException("크기 정보를 찾을 수 없습니다."));

            PetSitterPetSize petSitterPetSize = PetSitterPetSize.builder()
                .petSitter(petSitter)
                .petSize(petSize)
                .build();

            petSitterPetSizeRepository.save(petSitterPetSize);
        }

        // 4. 서비스 및 요금 정보 추가
        for (CreatePetSitterRequest.PetServiceRequest serviceRequest : request.getPetServices()) {
            PetService petService = petServiceRepository.findById(serviceRequest.getPetServiceId())
                .orElseThrow(() -> new IllegalArgumentException("서비스 정보를 찾을 수 없습니다."));

            PetSitterPetService petSitterPetService = PetSitterPetService.builder()
                .petSitter(petSitter)
                .petService(petService)
                .hprice(serviceRequest.getPrice())
                .build();

            petSitterPetServiceRepository.save(petSitterPetService);
        }

        // 5. 근무 가능 시간 정보 추가
        for (CreatePetSitterRequest.CareTimeRequest timeRequest : request.getCareTimes()) {
            PetCareTime petCareTime = PetCareTime.builder()
                .petSitter(petSitter)
                .weekday(timeRequest.getWeekday())
                .cateTime(PetCareTime.CateTime.valueOf(timeRequest.getCateTime()))
                .build();

            petCareTimeRepository.save(petCareTime);
        }

        return PetSitterResponse.from(petSitter);
    }

    public List<PetSitterResponse> getAllPetSitters() {
        return petSitterRepository.findAll().stream()
            .map(PetSitterResponse::from)
            .collect(Collectors.toList());
    }

    public PetSitterResponse getPetSitter(Long id) {
        PetSitter petSitter = petSitterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 펫시터를 찾을 수 없습니다."));
        return PetSitterResponse.from(petSitter);
    }

    public PetSitterResponse getPetSitterByUserId(Long userId) {
        PetSitter petSitter = petSitterRepository.findByUserId(userId)
            .orElse(null);

        return petSitter != null ? PetSitterResponse.from(petSitter) : null;
    }
}
