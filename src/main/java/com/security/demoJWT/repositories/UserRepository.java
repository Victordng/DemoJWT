package com.security.demoJWT.repositories;

import com.security.demoJWT.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
