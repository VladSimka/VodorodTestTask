package com.vladsimonenko.vodorodtesttask.controller;

import com.vladsimonenko.vodorodtesttask.dto.RateLoadingRequestDto;
import com.vladsimonenko.vodorodtesttask.dto.RateResponseDto;
import com.vladsimonenko.vodorodtesttask.dto.RateSaveResponseDto;
import com.vladsimonenko.vodorodtesttask.mapper.RateMapper;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import com.vladsimonenko.vodorodtesttask.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/rates")
@RequiredArgsConstructor
public class RateRestControllerV1 {

    private final RateService rateService;
    private final RateMapper rateMapper;
    private final MessageSource messageSource;

    @PostMapping("/load")
    public ResponseEntity<RateSaveResponseDto> getAllRatesByDate(@RequestBody @Validated RateLoadingRequestDto dto,
                                                                 Locale locale) {
        List<RateEntity> entityList = rateService.saveAllRatesByDateFromExternalApi(dto);
        String messageProps = !entityList.isEmpty() ? "rates.save.message" : "rates.save.empty";
        String message = messageSource.getMessage(messageProps, new Object[0], locale);
        RateSaveResponseDto response = new RateSaveResponseDto(message);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RateResponseDto> getRateByDateAndCode(@RequestParam(required = false, value = "date")
                                                                String date,

                                                                @RequestParam(required = false,
                                                                        value = "currencyId")
                                                                Integer id,

                                                                @RequestParam(required = false,
                                                                        value = "currencyAbbreviation")
                                                                String abbreviation) {

        RateEntity entity = rateService.getRateByDateAndCode(date, id, abbreviation);

        return ResponseEntity.ok(rateMapper.toDto(entity));
    }
}
