package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rolegroup")
public class RoleGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupname;
    private String description;

    @OneToMany(mappedBy = "roleGroup")
    private List<RoleGroupRole> roleGroupRoles = new ArrayList<>();

    @OneToMany(mappedBy = "roleGroup")
    private List<UserRoleGroup> userRoleGroups = new ArrayList<>();
}
