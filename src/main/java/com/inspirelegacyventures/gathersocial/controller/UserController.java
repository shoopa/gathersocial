package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.dto.UpdateLocationRequest;
import com.inspirelegacyventures.gathersocial.dto.UpdatePreferencesRequest;
import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import com.inspirelegacyventures.gathersocial.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/homeLocation")
    public ResponseEntity<User> updateUserHomeLocation(@PathVariable Long id, @RequestBody UpdateLocationRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setHomeLocation(request.getLocation());
                    userRepository.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/currentLocation")
    public ResponseEntity<User> updateUserCurrentLocation(@PathVariable Long id, @RequestBody UpdateLocationRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setCurrentLocation(request.getLocation());
                    userRepository.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<User> updateUserPreferences(@PathVariable Long id, @RequestBody UpdatePreferencesRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.getActivityPreferences().put(ActivityType.valueOf(request.getActivityType().toUpperCase()), request.getPreferences());
                    userRepository.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/groups")
    public ResponseEntity<Page<GroupDTO>> getUserGroups(@PathVariable Long userId, Pageable pageable) {
        Page<GroupDTO> groups = groupService.getGroupsByUserId(userId, pageable);
        return ResponseEntity.ok(groups);
    }
}
