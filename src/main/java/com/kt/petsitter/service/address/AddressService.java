package com.kt.petsitter.service.address;

import com.kt.petsitter.dto.address.response.AddressResponse;
import com.kt.petsitter.repository.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    private final AddressRepository addressRepository;

    public List<AddressResponse> getAddresses() {
        return addressRepository.findAll().stream()
            .map(AddressResponse::from)
            .toList();
    }
}
