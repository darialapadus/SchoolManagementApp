package com.example.school_managementv1.security;

import com.example.school_managementv1.entity.AppUser;
import com.example.school_managementv1.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!appUserRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "ROLE_ADMIN"
            );
            appUserRepository.save(admin);
            log.info("Default admin user created: username=admin, password=admin123");
        }

        if (!appUserRepository.existsByUsername("user")) {
            AppUser user = new AppUser(
                    "user",
                    passwordEncoder.encode("user123"),
                    "ROLE_USER"
            );
            appUserRepository.save(user);
            log.info("Default user created: username=user, password=user123");
        }
    }
}
