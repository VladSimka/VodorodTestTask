package com.vladsimonenko.vodorodtesttask.service;

import com.vladsimonenko.vodorodtesttask.dto.RateLoadingRequestDto;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;

import java.util.List;

public interface RateService {
    List<RateEntity> saveAllRatesByDateFromExternalApi(RateLoadingRequestDto dto);

    RateEntity getRateByDateAndCode(String date, Integer id, String abbreviation);
}
