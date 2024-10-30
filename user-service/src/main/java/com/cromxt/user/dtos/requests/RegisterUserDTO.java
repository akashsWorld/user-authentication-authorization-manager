package com.cromxt.user.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.EmbeddableInstantiator;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;



public record RegisterUserDTO(
        @NotBlank
        String email,
        @Length(min = 8, max = 16)
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String gender,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
        String birthDate,
        @Valid
        RecoveryAccountDetailsDTO recoveryAccountDetails

) {
}
