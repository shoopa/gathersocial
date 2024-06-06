package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.model.Activity;
import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.Group;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.ActivityRepository;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping
    public ResponseEntity<Object> createActivity(@RequestBody Activity activity) {
        try {
            if (activity.getType() == null || !isValidActivityType(activity.getType())) {
                return new ResponseEntity<>("Invalid activity type", HttpStatus.BAD_REQUEST);
            }

            Group group = groupRepository.findById(activity.getGroup().getId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            User host = userRepository.findById(activity.getHost().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            activity.setGroup(group);
            activity.setHost(host);
            Activity savedActivity = activityRepository.save(activity);
            return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Activity already exists for the specified group, date, and type", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        return activityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private boolean isValidActivityType(ActivityType type) {
        for (ActivityType t : ActivityType.values()) {
            if (t == type) {
                return true;
            }
        }
        return false;
    }
}
