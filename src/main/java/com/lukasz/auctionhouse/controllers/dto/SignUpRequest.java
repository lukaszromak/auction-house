package com.lukasz.auctionhouse.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {

    @Schema(example = "user3")
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @Schema(example = "user3")
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @Schema(example = "user3@mycompany.com")
    @Email
    private String email;
}