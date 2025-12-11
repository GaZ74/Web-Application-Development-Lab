package com.example.securecustomerapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashPasswordGenerator {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }
}
