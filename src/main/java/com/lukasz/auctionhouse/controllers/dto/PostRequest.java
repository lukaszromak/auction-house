package com.lukasz.auctionhouse.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {

    @Schema(example = "title")
    @NotBlank
    private String title;

    @Schema(example = "post contents")
    @NotBlank
    private String content;

}
