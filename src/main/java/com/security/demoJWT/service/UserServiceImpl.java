package com.security.demoJWT.service;

import com.security.demoJWT.entities.User;
import com.security.demoJWT.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
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
}
