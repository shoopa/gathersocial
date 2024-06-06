package com.inspirelegacyventures.gathersocial.repository;

import com.inspirelegacyventures.gathersocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
