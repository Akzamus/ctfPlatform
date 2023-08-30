package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
