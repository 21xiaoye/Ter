package com.cabin.ter.common.security;

public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return org.springframework.security.crypto.password.PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
