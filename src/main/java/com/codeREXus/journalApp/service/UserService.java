package com.codeREXus.journalApp.service;

import com.codeREXus.journalApp.Entity.User;
import com.codeREXus.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveEntry(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("user"));
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId id) {
        return userRepository.findById(id);
    }


    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
}