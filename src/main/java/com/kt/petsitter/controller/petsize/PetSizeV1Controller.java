package com.kt.petsitter.controller.petsize;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.dto.petsize.response.PetSizeResponse;
import com.kt.petsitter.service.petsize.PetSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/pet-sizes")
@RequiredArgsConstructor
public class PetSizeV1Controller {
    private final PetSizeService petSizeService;

    @GetMapping
    public ResponseEntity<RestResponse<List<PetSizeResponse>>> getPetSizes() {
        List<PetSizeResponse> petSizes = petSizeService.getPetSizes();
        return ResponseEntity.ok(RestResponse.success(petSizes, "펫 사이즈 목록 조회 성공"));
    }
}
