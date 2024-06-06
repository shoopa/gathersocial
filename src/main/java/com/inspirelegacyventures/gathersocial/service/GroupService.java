package com.inspirelegacyventures.gathersocial.service;

import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<GroupDTO> getGroupsByUserId(Long userId) {
        return groupRepository.findAllByUserId(userId);
    }

    public Page<GroupDTO> getGroupsByUserId(Long userId, Pageable pageable) {
        return groupRepository.findAllByUserId(userId, pageable);
    }
}
