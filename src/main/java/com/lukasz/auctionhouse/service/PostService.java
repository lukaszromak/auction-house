package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.controllers.dto.PostRequest;
import com.lukasz.auctionhouse.domain.Post;
import com.lukasz.auctionhouse.repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void deletePost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if(postOptional.isEmpty()){
            throw new EntityNotFoundException(String.format("Post with id %d not found", postId));
        }

        postRepository.delete(postOptional.get());
    }
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public Post updatePost(Long postId, PostRequest postRequest) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if(postOptional.isEmpty()){
            throw new EntityNotFoundException(String.format("Post with id %d not found", postId));
        }

        Post post = postOptional.get();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        return postRepository.save(post);
    }
}
