package com.example.security1.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role; //ROLE_USER,ROLE_ADMIN
    private String provider;
    private String provideId;
    private LocalDateTime createDate;

    public User() {
    }

    @Builder
    public User(String username, String password, String email, UserRole role, String provider, String provideId, LocalDateTime createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.provideId = provideId;
        this.createDate = createDate;
    }
}
