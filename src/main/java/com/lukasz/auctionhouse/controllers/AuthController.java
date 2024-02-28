package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.dto.AuthResponse;
import com.lukasz.auctionhouse.controllers.dto.LoginRequest;
import com.lukasz.auctionhouse.controllers.dto.SignUpRequest;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.DuplicatedUserInfoException;
import com.lukasz.auctionhouse.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        Optional<User> userOptional = userService.validUserNameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return ResponseEntity.ok(new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userService.hasUserWithUsername(signUpRequest.getUsername())){
            throw new DuplicatedUserInfoException("User with that email / username already exists.");
        }
        if(userService.hasUserWithEmail(signUpRequest.getEmail())){
            throw new DuplicatedUserInfoException("User with that email / username already exists.");
        }

        User user = userService.createUser(createUser(signUpRequest));
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    private User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setEnabled(true);
        return user;
    }

}
