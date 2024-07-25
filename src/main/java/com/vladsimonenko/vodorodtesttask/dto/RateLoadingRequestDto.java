package com.vladsimonenko.vodorodtesttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(name = "RateLoadingRequestDto")
public class RateLoadingRequestDto {

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "{errors.load_request}")
    @Schema(example = "2023-10-11")
    private String date;
}
