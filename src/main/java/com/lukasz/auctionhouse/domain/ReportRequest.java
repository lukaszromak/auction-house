package com.lukasz.auctionhouse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ReportRequest implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String requestedBy;

}
