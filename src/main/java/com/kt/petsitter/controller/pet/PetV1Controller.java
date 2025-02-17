package com.kt.petsitter.controller.pet;

import com.kt.petsitter.dto.pet.request.CreatePetRequest;
import com.kt.petsitter.dto.pet.request.UpdatePetRequest;
import com.kt.petsitter.dto.pet.response.PetResponse;
import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.global.annotation.login.SessionLogin;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.pet.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users/{userId}/pets")
@RequiredArgsConstructor
public class PetV1Controller {
    private final PetService petService;

    @PostMapping
    public ResponseEntity<RestResponse<PetResponse>> createPet(
        @PathVariable Long userId,
        @RequestBody CreatePetRequest request,
        @SessionLogin EmailLoginUserDto sessionUser
    ) {
        PetResponse response = petService.createPet(userId, request, sessionUser);
        return ResponseEntity.ok(RestResponse.success(response, "반려동물 등록이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<PetResponse>>> getPets(
        @PathVariable Long userId,
        @SessionLogin EmailLoginUserDto sessionUser
    ) {
        List<PetResponse> responses = petService.getPetsByUserId(userId, sessionUser);
        return ResponseEntity.ok(RestResponse.success(responses, "반려동물 목록 조회 성공"));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<RestResponse<PetResponse>> updatePet(
        @PathVariable Long userId,
        @PathVariable Long petId,
        @RequestBody UpdatePetRequest request,
        @SessionLogin EmailLoginUserDto sessionUser
    ) {
        PetResponse response = petService.updatePet(userId, petId, request, sessionUser);
        return ResponseEntity.ok(RestResponse.success(response, "반려동물 정보가 수정되었습니다."));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<RestResponse<Void>> deletePet(
        @PathVariable Long userId,
        @PathVariable Long petId,
        @SessionLogin EmailLoginUserDto sessionUser
    ) {
        petService.deletePet(userId, petId, sessionUser);
        return ResponseEntity.ok(RestResponse.success(null, "반려동물이 삭제되었습니다."));
    }
}
