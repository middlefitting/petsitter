package com.kt.petsitter.dto.petsitter.response;

import com.kt.petsitter.entity.PetCareTime;
import com.kt.petsitter.entity.PetSitter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Getter
@Builder
public class PetSitterResponse {
    private Long id;
    private String name;
    private String location;
    private String phone;
    private List<ServiceInfo> services;
    private List<PetTypeInfo> petTypes;
    private List<String> petSizes;
    private Map<String, boolean[]> workingHours;  // 요일별 24시간 배열

    @Getter
    @Builder
    public static class ServiceInfo {
        private String type;
        private Long price;
    }

    @Getter
    @Builder
    public static class PetTypeInfo {
        private String groupName;    // PetGroup의 이름
        private String groupCode;    // PetGroup의 코드
        private String typeName;     // PetGroupType의 이름
    }

    public static PetSitterResponse from(PetSitter petSitter) {
        // 근무 시간을 요일별 24시간 배열로 변환
        Map<String, boolean[]> workingHoursMap = new HashMap<>();
        String[] days = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
        for (String day : days) {
            workingHoursMap.put(day, new boolean[24]);
        }

        // PetCareTime 데이터를 workingHours 맵으로 변환
        for (PetCareTime careTime : petSitter.getPetCareTimes()) {
            String weekday = careTime.getWeekday().toLowerCase();
            String timeRange = careTime.getCateTime().toString();
            int startHour = Integer.parseInt(timeRange.substring(1, 3));
            int endHour = Integer.parseInt(timeRange.substring(3, 5));

            boolean[] dayHours = workingHoursMap.get(weekday);
            if (dayHours != null) {
                dayHours[startHour] = true;
            }
        }

        return PetSitterResponse.builder()
            .id(petSitter.getId())
            .name(petSitter.getName())
            .location(petSitter.getAddress().getCity())
            .phone(petSitter.getMobile())
            .services(petSitter.getPetSitterPetServices().stream()
                .map(service -> ServiceInfo.builder()
                    .type(service.getPetService().getServicename())
                    .price(service.getHprice())
                    .build())
                .collect(Collectors.toList()))
            .petTypes(petSitter.getPetSitterPetGroupTypes().stream()
                .map(type -> PetTypeInfo.builder()
                    .groupName(type.getPetGroupType().getPetGroup().getGroupname())
                    .groupCode(type.getPetGroupType().getPetGroup().getCode())
                    .typeName(type.getPetGroupType().getTypename())
                    .build())
                .collect(Collectors.toList()))
            .petSizes(petSitter.getPetSitterPetSizes().stream()
                .map(size -> size.getPetSize().getSizeInfo())
                .collect(Collectors.toList()))
            .workingHours(workingHoursMap)
            .build();
    }
}
