package com.kt.petsitter.dto.petsitter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePetSitterRequest {
    private Long userId;
    private Long addressId;
    private String name;
    private String mobile;
    private List<PetGroupTypeRequest> petGroupTypes;
    private List<Long> petSizes;
    private List<PetServiceRequest> petServices;
    private List<CareTimeRequest> careTimes;

    @Getter
    @NoArgsConstructor
    public static class PetGroupTypeRequest {
        private Long petGroupTypeId;
    }

    @Getter
    @NoArgsConstructor
    public static class PetServiceRequest {
        private Long petServiceId;
        private Long price;
    }

    @Getter
    @NoArgsConstructor
    public static class CareTimeRequest {
        private String weekday;
        private String cateTime;
    }
}
