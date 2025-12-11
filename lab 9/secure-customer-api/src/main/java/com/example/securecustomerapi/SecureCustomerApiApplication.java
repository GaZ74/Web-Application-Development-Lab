package com.example.securecustomerapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecureCustomerApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecureCustomerApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String plainPassword = "admin";
        String hashedPassword = encoder.encode(plainPassword);

        System.out.println("Hashed Password: " + hashedPassword);
    }
}
