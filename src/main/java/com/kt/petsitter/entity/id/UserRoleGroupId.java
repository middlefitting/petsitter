package com.example.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class UserRoleGroupId implements Serializable {
    private Long user;
    private Long roleGroup;
}
