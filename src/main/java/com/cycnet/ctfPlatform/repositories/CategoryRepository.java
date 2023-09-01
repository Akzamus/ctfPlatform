package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
