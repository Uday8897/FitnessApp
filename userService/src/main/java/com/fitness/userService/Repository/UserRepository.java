package com.fitness.userService.Repository;

import com.fitness.userService.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmailId(String email);

    boolean existsByKeyCloakId(String keyCloakId);

    User findByEmailId(String email);
}
