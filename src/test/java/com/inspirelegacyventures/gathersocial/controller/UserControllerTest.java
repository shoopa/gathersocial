package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.*;
import com.inspirelegacyventures.gathersocial.model.*;
import com.inspirelegacyventures.gathersocial.repository.*;
import com.inspirelegacyventures.gathersocial.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<Object> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetUser() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testUpdateUserHomeLocation() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setLocation("New Location");

        ResponseEntity<User> response = userController.updateUserHomeLocation(1L, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Location", response.getBody().getHomeLocation());
    }

    @Test
    void testGetUserGroups() {
        Page<GroupDTO> groupPage = new PageImpl<>(Collections.singletonList(new GroupDTO(1L, "Test Group")));
        when(groupService.getGroupsByUserId(anyLong(), any(Pageable.class))).thenReturn(groupPage);

        ResponseEntity<Page<GroupDTO>> response = userController.getUserGroups(1L, PageRequest.of(0, 10));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }
}
