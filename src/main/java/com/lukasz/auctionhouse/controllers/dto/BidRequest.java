package com.lukasz.auctionhouse.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BidRequest {

    @Schema(example = "1")
    @NotNull
    private Long bidId;

    @Schema(example = "4.34")
    @NotNull
    private Float offer;

}
