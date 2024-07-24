package com.vladsimonenko.vodorodtesttask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateExternalApiDto {

    @JsonProperty(value = "Cur_ID")
    private Integer currencyId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "Date")
    private Date date;

    @JsonProperty(value = "Cur_Abbreviation")
    private String currencyAbbreviation;

    @JsonProperty(value = "Cur_Scale")
    private Integer currencyScale;

    @JsonProperty(value = "Cur_Name")
    private String currencyName;

    @JsonProperty(value = "Cur_OfficialRate")
    private Double currencyOfficialRate;
}
