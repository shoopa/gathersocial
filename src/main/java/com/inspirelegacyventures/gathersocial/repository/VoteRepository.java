package com.inspirelegacyventures.gathersocial.repository;

import com.inspirelegacyventures.gathersocial.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @EntityGraph(attributePaths = {"user", "activity"})
    Optional<Vote> findById(Long id);
}
