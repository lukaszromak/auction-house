package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.dto.PostRequest;
import com.lukasz.auctionhouse.domain.Post;
import com.lukasz.auctionhouse.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post createPost(@RequestHeader(name = "Authorization") String authData, @Valid @RequestBody PostRequest postRequest){
        return postService.savePost(createPost(postRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Post> getPosts(){
        return postService.getAllPosts();
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok(String.format("Post with id %d deleted", postId));
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest){
        return postService.updatePost(postId, postRequest);
    }

    private Post createPost(PostRequest postRequest){
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        return post;
    }

}
