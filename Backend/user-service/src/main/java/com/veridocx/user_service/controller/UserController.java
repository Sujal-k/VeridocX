package com.veridocx.user_service.controller;


import com.veridocx.user_service.dto.UserDto;
import com.veridocx.user_service.entity.User;
import com.veridocx.user_service.mapper.UserMapper;
import com.veridocx.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService svc;
    private final UserMapper mapper;

    public UserController(UserService svc, UserMapper mapper) {
        this.svc = svc;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> list() {
        return ResponseEntity.ok(svc.listAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable UUID id) {
        User u = svc.findById(String.valueOf(id));
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapper.toDto(u));
    }
}
