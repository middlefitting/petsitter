package com.kt.petsitter.service.pet;

import com.kt.petsitter.dto.pet.request.CreatePetRequest;
import com.kt.petsitter.dto.pet.request.UpdatePetRequest;
import com.kt.petsitter.dto.pet.response.PetResponse;
import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.entity.Pet;
import com.kt.petsitter.entity.PetGroupType;
import com.kt.petsitter.entity.User;
import com.kt.petsitter.repository.pet.PetRepository;
import com.kt.petsitter.repository.petgrouptype.PetGroupTypeRepository;
import com.kt.petsitter.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetGroupTypeRepository petGroupTypeRepository;

    @Transactional
    public PetResponse createPet(Long userId, CreatePetRequest request, EmailLoginUserDto sessionUser) {
        if (!userId.equals(sessionUser.getUserId())) {
            throw new IllegalArgumentException("자신의 반려동물만 등록할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        PetGroupType petGroupType = petGroupTypeRepository.findById(request.getPetGroupTypeId())
            .orElseThrow(() -> new IllegalArgumentException("품종 정보를 찾을 수 없습니다."));

        Pet pet = Pet.builder()
            .name(request.getName())
            .age(request.getAge())
            .imageUrl(request.getImageUrl())
            .petsize(request.getPetsize())
            .user(user)
            .petGroupType(petGroupType)
            .build();

        pet = petRepository.save(pet);
        return PetResponse.from(pet);
    }

    public List<PetResponse> getPetsByUserId(Long userId, EmailLoginUserDto sessionUser) {
        if (!userId.equals(sessionUser.getUserId())) {
            throw new IllegalArgumentException("자신의 반려동물만 조회할 수 있습니다.");
        }

        return petRepository.findByUserId(userId).stream()
            .map(PetResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public PetResponse updatePet(Long userId, Long petId, UpdatePetRequest request, EmailLoginUserDto sessionUser) {
        if (!userId.equals(sessionUser.getUserId())) {
            throw new IllegalArgumentException("자신의 반려동물만 수정할 수 있습니다.");
        }

        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new IllegalArgumentException("반려동물을 찾을 수 없습니다."));

        if (!pet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("자신의 반려동물만 수정할 수 있습니다.");
        }

        PetGroupType petGroupType = petGroupTypeRepository.findById(request.getPetGroupTypeId())
            .orElseThrow(() -> new IllegalArgumentException("품종 정보를 찾을 수 없습니다."));

        pet.update(request.getName(), request.getAge(), request.getImageUrl(),
                  request.getPetsize(), petGroupType);

        return PetResponse.from(pet);
    }

    @Transactional
    public void deletePet(Long userId, Long petId, EmailLoginUserDto sessionUser) {
        if (!userId.equals(sessionUser.getUserId())) {
            throw new IllegalArgumentException("자신의 반려동물만 삭제할 수 있습니다.");
        }

        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new IllegalArgumentException("반려동물을 찾을 수 없습니다."));

        if (!pet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("자신의 반려동물만 삭제할 수 있습니다.");
        }

        petRepository.delete(pet);
    }
}
