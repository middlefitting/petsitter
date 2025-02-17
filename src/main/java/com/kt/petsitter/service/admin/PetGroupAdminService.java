package com.kt.petsitter.service.admin;

import com.kt.petsitter.dto.petgroup.request.CreatePetGroupRequest;
import com.kt.petsitter.dto.petgroup.request.UpdatePetGroupRequest;
import com.kt.petsitter.dto.petgroup.response.PetGroupResponse;
import com.kt.petsitter.entity.PetGroup;
import com.kt.petsitter.repository.petgroup.PetGroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetGroupAdminService {

    private final PetGroupRepository petGroupRepository;

    public List<PetGroupResponse> getAll() {
        return petGroupRepository.findAll().stream()
                .map(PetGroupResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetGroupResponse create(CreatePetGroupRequest request) {
        PetGroup petGroup = PetGroup.builder()
                .groupname(request.getGroupname())
                .code(request.getCode())
                .build();

        return PetGroupResponse.from(petGroupRepository.save(petGroup));
    }

    @Transactional
    public PetGroupResponse update(Long id, UpdatePetGroupRequest request) {
        PetGroup petGroup = petGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet group not found"));

        petGroup.update(request.getGroupname(), request.getCode());

        return PetGroupResponse.from(petGroup);
    }

    @Transactional
    public void delete(Long id) {
        PetGroup petGroup = petGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet group not found"));

        petGroupRepository.delete(petGroup);
    }
}
