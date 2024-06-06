package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.dto.*;
import com.inspirelegacyventures.gathersocial.model.*;
import com.inspirelegacyventures.gathersocial.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupControllerTest {

    @InjectMocks
    private GroupController groupController;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGroupsByUserId() {
        Page<GroupDTO> groupPage = new PageImpl<>(Collections.singletonList(new GroupDTO(1L, "Test Group")));
        when(groupRepository.findAllByUserId(anyLong(), any(Pageable.class))).thenReturn(groupPage);

        ResponseEntity<List<GroupDTO>> response = groupController.getGroupsByUserId(1L, 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreateGroup() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Test Group");
        request.setMemberIds(new HashSet<>(Arrays.asList(1L, 2L)));

        Group group = new Group();
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        ResponseEntity<Object> response = groupController.createGroup(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateGroup() {
        UpdateGroupRequest request = new UpdateGroupRequest();
        request.setName("Updated Group");
        request.setMemberIds(new HashSet<>(Arrays.asList(1L, 2L)));

        Group group = new Group();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        ResponseEntity<Group> response = groupController.updateGroup(1L, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
