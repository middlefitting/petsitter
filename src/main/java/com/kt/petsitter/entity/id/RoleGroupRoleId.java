package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class RoleGroupRoleId implements Serializable {
    private Long roleGroup;
    private Long role;
}
