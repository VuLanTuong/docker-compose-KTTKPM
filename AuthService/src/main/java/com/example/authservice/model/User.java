package com.example.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, name = "first_name")
    private String firstName;
    @Column(length = 50, name = "middle_name")
    private String middleName;
    @Column(length = 50, name = "last_name")
    private String lastName;
    @Column(length = 15)
    private String mobile;
    @Column(length = 50)
    private String email;
    @Column(length = 64, name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;
    @Column(name = "last_login")
    private Instant lastLogin;
    @Column(columnDefinition = "text")
    private String intro;
    @Column(columnDefinition = "text")
    private String profile;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<PostComment> comments;
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private Set<Post> posts;

    public User(Long id) {
        this.id = id;
    }

    public User(String firstName, String middleName, String lastName, String mobile, String email, String passwordHash,
            Instant registeredAt, Instant lastLogin, String intro, String profile) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registeredAt = registeredAt;
        this.lastLogin = lastLogin;
        this.intro = intro;
        this.profile = profile;
    }
}
