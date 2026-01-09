package com.veridocx.user_service.service;

import com.veridocx.user_service.entity.User;
import com.veridocx.user_service.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User create(User u) { return repo.save(u); }
    public List<User> listAll() { return repo.findAll(); }
    public User findById(String id) { return repo.findById(id).orElse(null); }
    public User findByEmail(String email) { return repo.findByEmail(email).orElse(null); }
}
