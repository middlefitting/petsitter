package com.kt.petsitter.controller.petgrouptype;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.dto.petgrouptype.response.PetGroupTypeResponse;
import com.kt.petsitter.service.petgrouptype.PetGroupTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pet-groups")
@RequiredArgsConstructor
public class PetGroupTypeV1Controller {
    private final PetGroupTypeService petGroupTypeService;

    @GetMapping("/{groupId}/breeds")
    public ResponseEntity<RestResponse<List<PetGroupTypeResponse>>> getPetGroupTypesByGroupId(
        @PathVariable Long groupId
    ) {
        List<PetGroupTypeResponse> breeds = petGroupTypeService.getPetGroupTypesByGroupId(groupId);
        return ResponseEntity.ok(RestResponse.success(breeds, "품종 목록 조회 성공"));
    }
}
