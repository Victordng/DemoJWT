package com.security.demoJWT.service;

import com.security.demoJWT.entities.User;

import java.util.List;


public interface UserService {

    User saveUser(User user);
    User updateUser(User user, Long id);
    void deleteUser(Long id);
    User findById(Long id);
    List<User> findAllUsers();
    void addCourseToUser(Long courseId, String matricule);
    void deleteCourseToUser(Long courseId, String matricule);
}
