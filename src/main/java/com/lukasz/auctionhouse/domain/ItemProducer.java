package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_producers")
@NoArgsConstructor
@Getter
@Setter
public class ItemProducer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_producer_id")
    private Long id;

    @Column(unique = true)
    private String name;

    public ItemProducer(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s", this.name);
    }
}
