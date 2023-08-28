package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
