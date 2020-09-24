package com.example.todo.repositories;

import com.example.todo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Task findByName(String name);
    Task findById(long id);
}
