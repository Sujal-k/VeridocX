package com.veridocx.user_service.mapper;


import com.veridocx.user_service.dto.UserDto;
import com.veridocx.user_service.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public UserDto toDto(User u) {
        if (u == null) return null;
        return UserDto.builder()
                .id(UUID.fromString(u.getId()))
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .build();
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .id(String.valueOf(dto.getId()))
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}
