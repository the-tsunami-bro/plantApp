package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) throws Exception{
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new Exception("Username is already taken");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new Exception("Email is already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }



}
