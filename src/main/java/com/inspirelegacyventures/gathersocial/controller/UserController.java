package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.dto.UpdateDynamicPreferencesRequest;
import com.inspirelegacyventures.gathersocial.dto.UpdateLocationRequest;
import com.inspirelegacyventures.gathersocial.dto.UpdatePreferencesRequest;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.service.GroupService;
import com.inspirelegacyventures.gathersocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/homeLocation")
    public ResponseEntity<User> updateUserHomeLocation(@PathVariable Long id, @RequestBody UpdateLocationRequest request) {
        return userService.updateUserHomeLocation(id, request.getLocation())
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/currentLocation")
    public ResponseEntity<User> updateUserCurrentLocation(@PathVariable Long id, @RequestBody UpdateLocationRequest request) {
        return userService.updateUserCurrentLocation(id, request.getLocation())
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/activityPreferences")
    public ResponseEntity<User> updateUserPreferences(@PathVariable Long id, @RequestBody UpdatePreferencesRequest request) {
        return userService.updateUserPreferences(id, request.getActivityType(), request.getPreferences())
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/groups")
    public ResponseEntity<Page<GroupDTO>> getUserGroups(@PathVariable Long userId, Pageable pageable) {
        Page<GroupDTO> groups = groupService.getGroupsByUserId(userId, pageable);
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}/dynamicPreferences")
    public ResponseEntity<User> updateDynamicPreferences(@PathVariable Long id, @RequestBody UpdateDynamicPreferencesRequest request) {
        try {
            User updatedUser = userService.updateDynamicPreferences(id, request);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
