package com.kt.petsitter.controller.address;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.dto.address.response.AddressResponse;
import com.kt.petsitter.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressV1Controller {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<RestResponse<List<AddressResponse>>> getAddresses() {
        List<AddressResponse> addresses = addressService.getAddresses();
        return ResponseEntity.ok(RestResponse.success(addresses, "주소 목록 조회 성공"));
    }
}
