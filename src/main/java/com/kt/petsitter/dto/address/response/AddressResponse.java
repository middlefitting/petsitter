package com.kt.petsitter.dto.address.response;

import com.kt.petsitter.entity.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {
    private Long id;
    private String city;

    public static AddressResponse from(Address address) {
        return AddressResponse.builder()
            .id(address.getId())
            .city(address.getCity())
            .build();
    }
}
