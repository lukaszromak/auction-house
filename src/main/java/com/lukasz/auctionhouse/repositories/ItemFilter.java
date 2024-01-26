package com.lukasz.auctionhouse.repositories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemFilter {

    private String namePhrase;
    private float fromStartPrice;
    private float ToStartPrice;
    private Date fromExpirationDate;
    private Date toExpirationDate;

}
