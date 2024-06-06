package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.model.*;
import com.inspirelegacyventures.gathersocial.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityControllerTest {

    @InjectMocks
    private ActivityController activityController;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateActivity() {
        Group group = new Group();
        group.setId(1L);
        User user = new User();
        user.setId(1L);

        Activity activity = new Activity();
        activity.setType(ActivityType.DINNER);
        activity.setGroup(group);
        activity.setHost(user);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        ResponseEntity<Object> response = activityController.createActivity(activity);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetActivity() {
        Activity activity = new Activity();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.of(activity));

        ResponseEntity<Activity> response = activityController.getActivity(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activity, response.getBody());
    }
}
