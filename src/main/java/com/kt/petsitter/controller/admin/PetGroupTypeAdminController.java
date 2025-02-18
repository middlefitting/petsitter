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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.petsitter.dto.petgrouptype.request.CreatePetGroupTypeRequest;
import com.kt.petsitter.dto.petgrouptype.request.UpdatePetGroupTypeRequest;
import com.kt.petsitter.dto.petgrouptype.response.PetGroupTypeResponse;
import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.global.annotation.login.AdminLogin;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.admin.PetGroupTypeAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/pet-group-types")
public class PetGroupTypeAdminController {

    private final PetGroupTypeAdminService petGroupTypeService;

    @GetMapping
    public ResponseEntity<RestResponse<List<PetGroupTypeResponse>>> getByGroupId(
            @RequestParam Long groupId, @AdminLogin EmailLoginUserDto adminUser) {
        return ResponseEntity.ok(RestResponse.success(petGroupTypeService.getByGroupId(groupId), "Pet Group Type"));
    }

    @PostMapping
    public ResponseEntity<RestResponse<PetGroupTypeResponse>> create(
            @RequestBody CreatePetGroupTypeRequest request, @AdminLogin EmailLoginUserDto adminUser) {
        return ResponseEntity.ok(RestResponse.success(petGroupTypeService.create(request), "Pet Group Type"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<PetGroupTypeResponse>> update(
            @PathVariable Long id, @AdminLogin EmailLoginUserDto adminUser,
            @RequestBody UpdatePetGroupTypeRequest request) {
        return ResponseEntity.ok(RestResponse.success(petGroupTypeService.update(id, request), "Pet Group Type"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> delete(@PathVariable Long id, @AdminLogin EmailLoginUserDto adminUser) {
        petGroupTypeService.delete(id);
        return ResponseEntity.ok(RestResponse.success(null, "Pet Group Type"));
    }
}
