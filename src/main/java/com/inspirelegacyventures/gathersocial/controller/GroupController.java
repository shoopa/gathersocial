package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.CreateGroupRequest;
import com.inspirelegacyventures.gathersocial.dto.UpdateGroupRequest;
import com.inspirelegacyventures.gathersocial.model.Group;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestBody CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.getName());

        Set<User> members = new HashSet<>();
        for (Long memberId : request.getMemberIds()) {
            userRepository.findById(memberId).ifPresent(user -> {
                members.add(user);
                user.addGroup(group); // Add group to user's set of groups
            });
        }
        group.setMembers(members);

        Group savedGroup = groupRepository.save(group);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody UpdateGroupRequest request) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    group.setName(request.getName());

                    // Clear existing members and add new members
                    Set<User> updatedMembers = new HashSet<>();
                    for (Long memberId : request.getMemberIds()) {
                        userRepository.findById(memberId).ifPresent(user -> {
                            updatedMembers.add(user);
                            user.addGroup(group);
                        });
                    }
                    group.setMembers(updatedMembers);

                    groupRepository.save(group);
                    return new ResponseEntity<>(group, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
