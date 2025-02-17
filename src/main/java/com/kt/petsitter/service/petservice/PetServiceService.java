package com.kt.petsitter.service.petservice;

import com.kt.petsitter.dto.petservice.response.PetServiceResponse;
import com.kt.petsitter.repository.petservice.PetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetServiceService {
    private final PetServiceRepository petServiceRepository;

    public List<PetServiceResponse> getPetServices() {
        return petServiceRepository.findAll().stream()
            .map(PetServiceResponse::from)
            .toList();
    }
}
