package com.gdg.jwt.repository;

import com.gdg.jwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
