package com.kt.petsitter.controller.petsitter;

import com.kt.petsitter.dto.petsitter.request.CreatePetSitterRequest;
import com.kt.petsitter.dto.petsitter.response.PetSitterResponse;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.petsitter.PetSitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/petsitters")
@RequiredArgsConstructor
public class PetSitterV1Controller {
    private final PetSitterService petSitterService;

    @PostMapping
    public ResponseEntity<RestResponse<PetSitterResponse>> createPetSitter(
        @RequestBody CreatePetSitterRequest request
    ) {
        PetSitterResponse response = petSitterService.createPetSitter(request);
        return ResponseEntity.ok(RestResponse.success(response, "펫시터 등록이 완료되었습니다."));
    }
}
