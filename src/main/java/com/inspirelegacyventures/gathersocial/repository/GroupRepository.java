package com.inspirelegacyventures.gathersocial.repository;

import com.inspirelegacyventures.gathersocial.dto.GroupDTO;
import com.inspirelegacyventures.gathersocial.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT new com.inspirelegacyventures.gathersocial.dto.GroupDTO(g.id, g.name) " +
            "FROM Group g JOIN g.members m WHERE m.id = :userId")
    List<GroupDTO> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.inspirelegacyventures.gathersocial.dto.GroupDTO(g.id, g.name) " +
            "FROM Group g JOIN g.members m WHERE m.id = :userId")
    Page<GroupDTO> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
