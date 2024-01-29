package com.lukasz.auctionhouse.domain;

import com.lukasz.auctionhouse.validators.AuctionDuration;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@NamedQuery(name = "Item.findAllByCategoryPhrase",
            query = "select i from Item i join i.itemCategory where lower(i.itemCategory.name) like lower(concat('%', ?1, '%'))")
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Size(min = 3, max = 100)
    private String name;

    @NumberFormat(pattern = "#.00")
    private Float startPrice;

    @NumberFormat(pattern = "#.00")
    private Float buyItNowPrice;

    @Size(max = 500)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @AuctionDuration
    private Date expirationDate;

    private boolean bought;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User listedBy;

    private String imagePath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ItemProducer> itemProducers;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

}
