package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByEventAndName(Event event, String name);
    Page<Task> findByCategory_Id(Long categoryId, Pageable pageable);

}
