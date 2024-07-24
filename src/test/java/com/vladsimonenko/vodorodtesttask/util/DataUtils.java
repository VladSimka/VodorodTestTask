package com.vladsimonenko.vodorodtesttask.util;

import com.vladsimonenko.vodorodtesttask.dto.RateExternalApiDto;
import com.vladsimonenko.vodorodtesttask.dto.RateResponseDto;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;

import java.sql.Date;

public class DataUtils {

    public static RateEntity getFirstRateTransient() {
        return RateEntity.builder()
                .currencyId(431)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(3.1467)
                .build();
    }

    public static RateEntity getSecondRateTransient() {
        return RateEntity.builder()
                .currencyId(331)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(4.1467)
                .build();
    }
    public static RateEntity getThirdRateTransient() {
        return RateEntity.builder()
                .currencyId(231)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(2.1467)
                .build();
    }

    public static RateEntity getFirstRatePersisted() {
        return RateEntity.builder()
                .id(1)
                .currencyId(431)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(3.1467)
                .build();
    }

    public static RateEntity getSecondRatePersisted() {
        return RateEntity.builder()
                .id(2)
                .currencyId(331)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(4.1467)
                .build();
    }
    public static RateEntity getThirdRatePersisted() {
        return RateEntity.builder()
                .id(3)
                .currencyId(231)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(2.1467)
                .build();
    }

    public static RateExternalApiDto getFirstRateApi(){
        return RateExternalApiDto.builder()
                .currencyId(431)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(3.1467)
                .build();
    }

    public static RateExternalApiDto getSecondRateApi() {
        return RateExternalApiDto.builder()
                .currencyId(331)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(4.1467)
                .build();
    }
    public static RateExternalApiDto getThirdRateApi() {
        return RateExternalApiDto.builder()
                .currencyId(231)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(2.1467)
                .build();
    }

    public static RateResponseDto getFirstRateDto() {
        return RateResponseDto.builder()
                .currencyId(231)
                .date(Date.valueOf("2024-07-24"))
                .currencyAbbreviation("USD")
                .currencyScale(1)
                .currencyName("Доллар США")
                .currencyOfficialRate(2.1467)
                .build();
    }
}
