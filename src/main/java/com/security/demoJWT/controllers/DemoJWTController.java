package com.security.demoJWT.controllers;

import com.security.demoJWT.entities.Course;
import com.security.demoJWT.entities.CourseCategory;
import com.security.demoJWT.entities.User;
import com.security.demoJWT.service.CourseCategoryService;
import com.security.demoJWT.service.CourseService;
import com.security.demoJWT.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class DemoJWTController {

    private final UserService userService;
    private final CourseService courseService;
    private final CourseCategoryService categoryService;


    @GetMapping(value = "/users",produces = {APPLICATION_JSON_VALUE})
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User findOne(@PathVariable("id") Long id){
        return userService.findById(id);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateOne(@PathVariable("id") Long id,@RequestBody User user ){
        return userService.updateUser(user,id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteOne(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }

    @GetMapping(value = "/courses",produces = {APPLICATION_JSON_VALUE})
    public List<Course> findAllCourses(){
        return courseService.findAllCourses();
    }

    @GetMapping("/courses/{id}")
    public Course findCourse(@PathVariable("id") Long id){
        return courseService.findById(id);
    }

    @PostMapping("/courses")
    public Course saveCourse(@RequestBody Course course){
        return courseService.saveCourse(course);
    }

    @PutMapping("/courses/{id}")
    public Course updateCourse(@PathVariable("id") Long id,@RequestBody Course course ){
        return courseService.updateCourse(course,id);
    }

    @DeleteMapping("/courses/{id}")
    public void deleteCourse(@PathVariable("id") Long id){
        courseService.deleteCourse(id);
    }

    @GetMapping(value = "/categories",produces = {APPLICATION_JSON_VALUE})
    public CourseCategory saveCategory(@RequestBody CourseCategory category) {
        return categoryService.saveCategory(category);
    }

    @PutMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public CourseCategory updateCategory(@RequestBody CourseCategory category, @PathVariable("id") Long id) {
        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public CourseCategory findById(@PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @GetMapping(value = "/categories",produces = {APPLICATION_JSON_VALUE})
    public List<CourseCategory> findAllCategories() {
        return categoryService.findAllCategories();
    }
}
