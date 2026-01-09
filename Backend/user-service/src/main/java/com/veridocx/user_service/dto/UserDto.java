package com.veridocx.user_service.dto;


import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
}

