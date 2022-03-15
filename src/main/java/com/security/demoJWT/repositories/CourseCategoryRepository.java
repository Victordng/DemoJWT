package com.security.demoJWT.repositories;

import com.security.demoJWT.entities.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
}
