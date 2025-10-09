package com.nurudeen.propertyfind.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    private final BCryptPasswordEncoder encoder;

    public PasswordUtils(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public boolean matches(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
