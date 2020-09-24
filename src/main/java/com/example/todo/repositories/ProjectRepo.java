package com.example.todo.repositories;

import com.example.todo.entities.Project;
import com.example.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository <Project, Long> {
    Project findByName(String name);
    Project findById(long id);
}
