package com.kt.petsitter.service.petgrouptype;

import com.kt.petsitter.dto.petgrouptype.response.PetGroupTypeResponse;
import com.kt.petsitter.repository.petgrouptype.PetGroupTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetGroupTypeService {
    private final PetGroupTypeRepository petGroupTypeRepository;

    public List<PetGroupTypeResponse> getPetGroupTypes() {
        return petGroupTypeRepository.findAll().stream()
            .map(PetGroupTypeResponse::from)
            .toList();
    }

    public List<PetGroupTypeResponse> getPetGroupTypesByGroupId(Long groupId) {
        return petGroupTypeRepository.findByPetGroupId(groupId).stream()
            .map(PetGroupTypeResponse::from)
            .toList();
    }
}
