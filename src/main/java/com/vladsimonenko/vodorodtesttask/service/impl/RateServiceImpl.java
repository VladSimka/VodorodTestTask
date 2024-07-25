package com.vladsimonenko.vodorodtesttask.service.impl;

import com.vladsimonenko.vodorodtesttask.client.RateClient;
import com.vladsimonenko.vodorodtesttask.dto.RateExternalApiDto;
import com.vladsimonenko.vodorodtesttask.dto.RateLoadingRequestDto;
import com.vladsimonenko.vodorodtesttask.exception.InvalidDateFormatException;
import com.vladsimonenko.vodorodtesttask.exception.InvalidParameterException;
import com.vladsimonenko.vodorodtesttask.exception.RatesAlreadyExistsException;
import com.vladsimonenko.vodorodtesttask.mapper.RateMapper;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import com.vladsimonenko.vodorodtesttask.repository.RateRepository;
import com.vladsimonenko.vodorodtesttask.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;

    private final RateClient rateClient;

    private final RateMapper rateMapper;

    @Override
    public List<RateEntity> saveAllRatesByDateFromExternalApi(RateLoadingRequestDto dto) {
        validateDate(dto.getDate());
        Date currentDate = Date.valueOf(dto.getDate());
        if (rateRepository.existsByDate(currentDate)) {
            throw new RatesAlreadyExistsException("errors.rates.exists_by_date");
        }

        List<RateExternalApiDto> allRatesDtoByDate =
                rateClient.getAllRatesByDate(currentDate, 0);

        List<RateEntity> rates = rateMapper.toEntity(allRatesDtoByDate);
        return rateRepository.saveAll(rates);
    }

    @Override
    public RateEntity getRateByDateAndCode(String stringDate, Integer id, String abbreviation) {
        validateParameters(stringDate, id, abbreviation);

        Date date = Date.valueOf(stringDate);
        return id != null
                ? getRateByDateAndCurrencyId(date, id)
                : getRateByDateAndCurrencyAbbreviation(date, abbreviation);
    }

    private RateEntity getRateByDateAndCurrencyId(Date date, Integer id) {
        Optional<RateEntity> byCurrencyIdAndDate = rateRepository.findByCurrencyIdAndDate(id, date);

        if (byCurrencyIdAndDate.isPresent()) {
            return byCurrencyIdAndDate.get();
        } else {
            RateExternalApiDto dto =
                    rateClient.getRateByDateAndParameter(id.toString(), 0, date);

            return rateMapper.toEntity(dto);
        }
    }


    private RateEntity getRateByDateAndCurrencyAbbreviation(Date date, String abbreviation) {
        Optional<RateEntity> byCurrencyAbbreviationAndDate =
                rateRepository.findByCurrencyAbbreviationAndDate(abbreviation, date);

        if (byCurrencyAbbreviationAndDate.isPresent()) {
            return byCurrencyAbbreviationAndDate.get();
        } else {
            RateExternalApiDto dto =
                    rateClient.getRateByDateAndParameter(abbreviation, 2, date);

            return rateMapper.toEntity(dto);
        }
    }

    private void validateParameters(String date, Integer id, String abbreviation) {
        validateDate(date);

        if (id == null && abbreviation == null) {
            throw new InvalidParameterException("errors.parameters.empty");
        }
    }

    private void validateDate(String date) {
        if (date == null) {
            throw new InvalidDateFormatException("errors.date.empty");
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("errors.date.format");
        }
    }
}
