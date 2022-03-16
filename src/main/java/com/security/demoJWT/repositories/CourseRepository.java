package com.security.demoJWT.repositories;

import com.security.demoJWT.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
