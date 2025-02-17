package com.kt.petsitter.controller.petgroup;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.dto.petgroup.response.PetGroupResponse;
import com.kt.petsitter.service.petgroup.PetGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/pet-groups")
@RequiredArgsConstructor
public class PetGroupV1Controller {
    private final PetGroupService petGroupService;

    @GetMapping
    public ResponseEntity<RestResponse<List<PetGroupResponse>>> getPetGroups() {
        List<PetGroupResponse> petGroups = petGroupService.getPetGroups();
        return ResponseEntity.ok(RestResponse.success(petGroups, "펫 그룹 목록 조회 성공"));
    }
}
