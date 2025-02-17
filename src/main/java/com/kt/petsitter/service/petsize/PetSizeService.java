package com.kt.petsitter.service.petsize;

import com.kt.petsitter.dto.petsize.response.PetSizeResponse;
import com.kt.petsitter.repository.petsize.PetSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetSizeService {
    private final PetSizeRepository petSizeRepository;

    public List<PetSizeResponse> getPetSizes() {
        return petSizeRepository.findAll().stream()
            .map(PetSizeResponse::from)
            .toList();
    }
}
