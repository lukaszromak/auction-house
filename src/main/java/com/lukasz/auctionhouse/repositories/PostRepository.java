package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
