package com.cromxt.user.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PhoneNumberDTO(
        @NotBlank
        String countryCode,
        @NotBlank
        String phoneNumber
) {
}
