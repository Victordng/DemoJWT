package com.security.demoJWT.service;

import com.security.demoJWT.entities.Course;
import com.security.demoJWT.entities.User;
import com.security.demoJWT.repositories.CourseRepository;
import com.security.demoJWT.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
       Optional<User> us = userRepository.findById(id);

       if(us.isPresent()){
           user.setUserId(id);
           userRepository.save(user);
       }else throw new RuntimeException("User not found!");
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> us = userRepository.findById(id);

        if(us.isPresent()){
            userRepository.deleteById(id);
        }else throw new RuntimeException("User not found!");
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }else throw new RuntimeException("User not found!");
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addCourseToUser(Long courseId, String matricule) {
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findUserByMatricule(matricule);
        if(course!=null && user!=null){
            user.getCourses().add(course);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteCourseToUser(Long courseId, String matricule) {
        Optional<Course> course = courseRepository.findById(courseId);
        User user = userRepository.findUserByMatricule(matricule);
        if(course.isPresent() && user!=null){
            int index = user.getCourses().indexOf(course.get().getCourseName());
            user.getCourses().remove(index+1);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if(user!=null){
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getUserType().name()));
            return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
        }else throw new UsernameNotFoundException(String.format("user with username %s not found!",username));
    }
}


