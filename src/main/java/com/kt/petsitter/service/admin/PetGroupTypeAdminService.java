package com.kt.petsitter.service.admin;

import com.kt.petsitter.dto.petgrouptype.request.CreatePetGroupTypeRequest;
import com.kt.petsitter.dto.petgrouptype.request.UpdatePetGroupTypeRequest;
import com.kt.petsitter.dto.petgrouptype.response.PetGroupTypeResponse;
import com.kt.petsitter.entity.PetGroup;
import com.kt.petsitter.entity.PetGroupType;
import com.kt.petsitter.repository.petgroup.PetGroupRepository;
import com.kt.petsitter.repository.petgrouptype.PetGroupTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetGroupTypeAdminService {

    private final PetGroupTypeRepository petGroupTypeRepository;
    private final PetGroupRepository petGroupRepository;

    public List<PetGroupTypeResponse> getByGroupId(Long groupId) {
        return petGroupTypeRepository.findByPetGroupId(groupId).stream()
                .map(PetGroupTypeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetGroupTypeResponse create(CreatePetGroupTypeRequest request) {
        PetGroup petGroup = petGroupRepository.findById(request.getPetGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Pet group not found"));

        PetGroupType petGroupType = PetGroupType.builder()
                .typename(request.getTypename())
                .petGroup(petGroup)
                .build();

        return PetGroupTypeResponse.from(petGroupTypeRepository.save(petGroupType));
    }

    @Transactional
    public PetGroupTypeResponse update(Long id, UpdatePetGroupTypeRequest request) {
        PetGroupType petGroupType = petGroupTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet group type not found"));

        petGroupType.update(request.getTypename());

        return PetGroupTypeResponse.from(petGroupType);
    }

    @Transactional
    public void delete(Long id) {
        PetGroupType petGroupType = petGroupTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet group type not found"));

        petGroupTypeRepository.delete(petGroupType);
    }
}
