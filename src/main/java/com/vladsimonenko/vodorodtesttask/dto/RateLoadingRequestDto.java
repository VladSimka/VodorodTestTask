package com.vladsimonenko.vodorodtesttask.dto;

import jakarta.validation.constraints.Pattern;

public record RateLoadingRequestDto(
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
                message = "{errors.load_request}")
        String date) {
}
