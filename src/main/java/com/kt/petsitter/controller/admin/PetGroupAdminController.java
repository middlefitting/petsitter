package com.kt.petsitter.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.petsitter.dto.petgroup.request.CreatePetGroupRequest;
import com.kt.petsitter.dto.petgroup.request.UpdatePetGroupRequest;
import com.kt.petsitter.dto.petgroup.response.PetGroupResponse;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.admin.PetGroupAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/pet-groups")
public class PetGroupAdminController {

    private final PetGroupAdminService petGroupService;

    @GetMapping
    public ResponseEntity<RestResponse<List<PetGroupResponse>>> getAll() {
        return ResponseEntity.ok(RestResponse.success(petGroupService.getAll(), "Pet Group"));
    }

    @PostMapping
    public ResponseEntity<RestResponse<PetGroupResponse>> create(@RequestBody CreatePetGroupRequest request) {
        return ResponseEntity.ok(RestResponse.success(petGroupService.create(request), "Pet Group"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<PetGroupResponse>> update(
            @PathVariable Long id,
            @RequestBody UpdatePetGroupRequest request) {
        return ResponseEntity.ok(RestResponse.success(petGroupService.update(id, request), "Pet Group"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> delete(@PathVariable Long id) {
        petGroupService.delete(id);
        return ResponseEntity.ok(RestResponse.success(null, "Pet Group"));
    }
}
