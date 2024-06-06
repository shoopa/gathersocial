package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.CreateGroupRequest;
import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.dto.UpdateGroupRequest;
import com.inspirelegacyventures.gathersocial.model.Group;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private static final Logger logger = Logger.getLogger(GroupController.class.getName());

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupDTO>> getGroupsByUserId(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupDTO> groupPage = groupRepository.findAllByUserId(userId, pageable);
        return ResponseEntity.ok(groupPage.getContent());
    }

    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestBody CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.getName());

        for (Long memberId : request.getMemberIds()) {
            userRepository.findById(memberId).ifPresent(group::addMember);
        }

        Group savedGroup = groupRepository.save(group);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody UpdateGroupRequest request) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setName(request.getName());

            // Remove existing members
            group.getMembers().clear();

            // Add new members
            for (Long memberId : request.getMemberIds()) {
                Optional<User> optionalUser = userRepository.findById(memberId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    group.addMember(user);
                } else {
                    logger.warning("User with ID " + memberId + " not found");
                }
            }

            Group savedGroup = groupRepository.save(group);
            return new ResponseEntity<>(savedGroup, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
