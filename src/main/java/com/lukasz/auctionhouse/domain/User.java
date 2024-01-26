package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(min = 4, max = 20)
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(max = 60)
    @NotBlank
    private String password;

    private Boolean enabled;

    @ManyToMany
    private Set<Role> roles;

    public User(String userName, String email, String password){
        this.username = userName;
        this.email = email;
        this.password = password;
        this.enabled = true;
    }

}
