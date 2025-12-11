package com.example.securecustomerapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.securecustomerapi.util.HashPasswordGenerator;

@SpringBootApplication
public class SecureCustomerApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecureCustomerApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String plainPassword = "admin";
        String hashedPassword = HashPasswordGenerator.hash(plainPassword);

        System.out.println("Hashed Password: " + hashedPassword);
    }
}
