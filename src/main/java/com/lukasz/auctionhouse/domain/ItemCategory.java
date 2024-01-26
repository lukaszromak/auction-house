package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_categories")
@NoArgsConstructor
@Getter
@Setter
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_category_id")
    public Long id;

    @Column(unique = true)
    public String name;

    public ItemCategory(String name){
        this.name = name;
    }

}
