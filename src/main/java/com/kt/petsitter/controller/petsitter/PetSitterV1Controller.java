package com.kt.petsitter.controller.petsitter;

import com.kt.petsitter.dto.petsitter.request.CreatePetSitterRequest;
import com.kt.petsitter.dto.petsitter.response.PetSitterResponse;
import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.global.annotation.login.Login;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.petsitter.PetSitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/petsitters")
@RequiredArgsConstructor
public class PetSitterV1Controller {
    private final PetSitterService petSitterService;

    @PostMapping
    public ResponseEntity<RestResponse<PetSitterResponse>> createPetSitter(
        @RequestBody CreatePetSitterRequest request, @Login EmailLoginUserDto sessionUser
    ) {
        PetSitterResponse response = petSitterService.createPetSitter(request);
        return ResponseEntity.ok(RestResponse.success(response, "펫시터 등록이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<PetSitterResponse>>> getAllPetSitters(@Login EmailLoginUserDto sessionUser) {
        List<PetSitterResponse> responses = petSitterService.getAllPetSitters();
        return ResponseEntity.ok(RestResponse.success(responses, "펫시터 목록 조회 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<PetSitterResponse>> getPetSitter(@PathVariable Long id, @Login EmailLoginUserDto sessionUser) {
        PetSitterResponse response = petSitterService.getPetSitter(id);
        return ResponseEntity.ok(RestResponse.success(response, "펫시터 상세 조회 성공"));
    }
}
