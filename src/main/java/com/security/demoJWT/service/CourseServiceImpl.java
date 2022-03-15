package com.security.demoJWT.service;

import com.security.demoJWT.entities.Course;
import com.security.demoJWT.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course, Long id) {
        Optional<Course> course1 = courseRepository.findById(id);

        if(course1.isPresent()){
            course.setCourseId(id);
            courseRepository.save(course);
        }else throw new RuntimeException("Course not found!");

        return course;
    }

    @Override
    public void deleteCourse(Long id) {
        Optional<Course> course1 = courseRepository.findById(id);

        if(course1.isPresent()){
            courseRepository.deleteById(id);
        }else throw new RuntimeException("Course not found!");

    }

    @Override
    public Course findById(Long id) {
        Optional<Course> course = courseRepository.findById(id);

        if(course.isPresent()){
            return course.get();
        }else throw new RuntimeException("Course not found!");
    }

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
}
