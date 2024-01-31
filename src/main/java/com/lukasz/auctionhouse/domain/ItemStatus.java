package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_status")
@NoArgsConstructor
@Getter
@Setter
public class ItemStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    public Long id;

    @Column(unique = true)
    public String name;

}
