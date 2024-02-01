package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@Getter
@Setter
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private Long bidsPlaced;

    private Date generatedDate;

    @ManyToOne
    private User generatedBy;
}
