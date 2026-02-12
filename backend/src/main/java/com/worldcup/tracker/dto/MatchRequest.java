package com.worldcup.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchRequest {

    @NotBlank(message = "Le nom de l'équipe A est obligatoire")
    private String homeTeam;

    @NotBlank(message = "Le nom de l'équipe B est obligatoire")
    private String awayTeam;

    @NotNull(message = "La date du match est obligatoire")
    @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
    private LocalDateTime datetime;

    @NotBlank(message = "La phase est obligatoire")
    private String phase;

}