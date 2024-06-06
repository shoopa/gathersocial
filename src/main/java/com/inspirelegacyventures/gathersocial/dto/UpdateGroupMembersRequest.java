package com.inspirelegacyventures.gathersocial.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateGroupMembersRequest {
    private Set<Long> memberIds;
}
