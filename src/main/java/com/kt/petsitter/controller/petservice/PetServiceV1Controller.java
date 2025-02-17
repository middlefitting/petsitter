package com.kt.petsitter.controller.petservice;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.dto.petservice.response.PetServiceResponse;
import com.kt.petsitter.service.petservice.PetServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/pet-services")
@RequiredArgsConstructor
public class PetServiceV1Controller {
    private final PetServiceService petServiceService;

    @GetMapping
    public ResponseEntity<RestResponse<List<PetServiceResponse>>> getPetServices() {
        List<PetServiceResponse> petServices = petServiceService.getPetServices();
        return ResponseEntity.ok(RestResponse.success(petServices, "펫 서비스 목록 조회 성공"));
    }
}
