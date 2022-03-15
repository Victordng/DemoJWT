package com.security.demoJWT.service;

import com.security.demoJWT.entities.Course;

import java.util.List;


public interface CourseService {

    Course saveCourse(Course course);
    Course updateCourse(Course course, Long id);
    void deleteCourse(Long id);
    Course findById(Long id);
    List<Course> findAllCourses();

}
