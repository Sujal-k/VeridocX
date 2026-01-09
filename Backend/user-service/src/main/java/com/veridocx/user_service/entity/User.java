package com.veridocx.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false, length = 36)
    private String id = UUID.randomUUID().toString();


    private String name;
    private String email;
    private String password;
    private String role;
}
