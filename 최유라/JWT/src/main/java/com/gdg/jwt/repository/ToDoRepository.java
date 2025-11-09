package com.gdg.jwt.repository;

import com.gdg.jwt.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
