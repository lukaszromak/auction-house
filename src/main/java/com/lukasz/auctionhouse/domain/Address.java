package com.lukasz.auctionhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 14)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 14)
    private String lastName;

    @NotNull
    @Size(max = 100)
    private String street;

    @NotNull
    @Size(max = 6)
    private String zipCode;

    @NotNull
    @Size(max = 100)
    private String city;

    @NotNull
    @Size(max = 100)
    private String country;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
