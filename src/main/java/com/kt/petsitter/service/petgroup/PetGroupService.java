package com.kt.petsitter.service.petgroup;

import com.kt.petsitter.dto.petgroup.response.PetGroupResponse;
import com.kt.petsitter.repository.petgroup.PetGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetGroupService {
    private final PetGroupRepository petGroupRepository;

    public List<PetGroupResponse> getPetGroups() {
        return petGroupRepository.findAll().stream()
            .map(PetGroupResponse::from)
            .toList();
    }
}
