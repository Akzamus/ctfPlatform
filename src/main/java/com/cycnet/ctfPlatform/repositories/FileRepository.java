package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.File;
import com.cycnet.ctfPlatform.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByTaskAndPath(Task task, String path);

}
