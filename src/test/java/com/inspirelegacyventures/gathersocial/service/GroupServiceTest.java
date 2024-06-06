package com.inspirelegacyventures.gathersocial.service;

import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGroupsByUserId() {
        Long userId = 1L;
        List<GroupDTO> mockGroupList = Arrays.asList(
                new GroupDTO(1L, "Group 1"),
                new GroupDTO(2L, "Group 2")
        );

        when(groupRepository.findAllByUserId(userId)).thenReturn(mockGroupList);

        List<GroupDTO> result = groupService.getGroupsByUserId(userId);

        assertEquals(2, result.size());
        assertEquals("Group 1", result.get(0).getName());
        assertEquals("Group 2", result.get(1).getName());
        verify(groupRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testGetGroupsByUserIdWithPagination() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<GroupDTO> mockGroupList = Arrays.asList(
                new GroupDTO(1L, "Group 1"),
                new GroupDTO(2L, "Group 2")
        );
        Page<GroupDTO> mockPage = new PageImpl<>(mockGroupList, pageable, mockGroupList.size());

        when(groupRepository.findAllByUserId(userId, pageable)).thenReturn(mockPage);

        Page<GroupDTO> result = groupService.getGroupsByUserId(userId, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Group 1", result.getContent().get(0).getName());
        assertEquals("Group 2", result.getContent().get(1).getName());
        verify(groupRepository, times(1)).findAllByUserId(userId, pageable);
    }
}
