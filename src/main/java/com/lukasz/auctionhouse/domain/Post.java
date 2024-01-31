package com.lukasz.auctionhouse.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lukasz.auctionhouse.serializers.CustomDateSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String title;

    @Column(length = 1000)
    @NotNull
    private String content;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdDate;

}
