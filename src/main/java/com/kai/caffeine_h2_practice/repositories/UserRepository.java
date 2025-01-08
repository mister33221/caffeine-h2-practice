package com.kai.caffeine_h2_practice.repositories;

import com.kai.caffeine_h2_practice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
