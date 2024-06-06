package com.inspirelegacyventures.gathersocial.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;

    public GroupDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
