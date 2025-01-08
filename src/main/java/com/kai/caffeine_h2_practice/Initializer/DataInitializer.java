package com.kai.caffeine_h2_practice.Initializer;

import com.kai.caffeine_h2_practice.models.User;
import com.kai.caffeine_h2_practice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        userRepository.save(User.builder().username("user1").build());
        userRepository.save(User.builder().username("user2").build());
        userRepository.save(User.builder().username("user3").build());
    }
}
