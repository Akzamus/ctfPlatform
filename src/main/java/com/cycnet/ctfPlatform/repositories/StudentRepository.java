package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
