package com.vladsimonenko.vodorodtesttask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsimonenko.vodorodtesttask.dto.RateLoadingRequestDto;
import com.vladsimonenko.vodorodtesttask.exception.InvalidDateFormatException;
import com.vladsimonenko.vodorodtesttask.mapper.RateMapper;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import com.vladsimonenko.vodorodtesttask.service.RateService;
import com.vladsimonenko.vodorodtesttask.util.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class RateRestControllerV1Tests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RateService rateService;

    @MockBean
    private RateMapper rateMapper;

    @Autowired
    private MessageSource messageSource;

    @Test
    @DisplayName("Test load rates by date functionality")
    public void givenDate_whenLoadRates_thenSuccess() throws Exception {
        //given
        var first = DataUtils.getFirstRatePersisted();
        BDDMockito.given(rateService.saveAllRatesByDateFromExternalApi(any()))
                .willReturn(List.of(first));
        String message = messageSource
                .getMessage("rates.save.message", new Object[0], new Locale("en"));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/rates/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RateLoadingRequestDto(first.getDate().toString()))));

        //then
        result.
                andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @DisplayName("Test can not find rates to load by date functionality")
    public void givenDate_whenCanNotFindRates_thenMessage() throws Exception {
        //given
        var first = DataUtils.getFirstRatePersisted();
        BDDMockito.given(rateService.saveAllRatesByDateFromExternalApi(any()))
                .willReturn(List.of());
        String message = messageSource
                .getMessage("rates.save.empty", new Object[0], new Locale("en"));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/rates/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RateLoadingRequestDto(first.getDate().toString()))));

        //then
        result.
                andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @DisplayName("Test get rate by date and code functionality")
    public void givenDateAndCode_whenGetRate_thenSuccess() throws Exception {
        //given
        var first = DataUtils.getFirstRateTransient();
        var dto = DataUtils.getFirstRateDto();
        BDDMockito.given(rateService.getRateByDateAndCode(any(), any(), any()))
                .willReturn(first);
        BDDMockito.given(rateMapper.toDto(any(RateEntity.class)))
                .willReturn(dto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("date", List.of(dto.getDate().toString()));
        params.put("currencyId", List.of(dto.getCurrencyId().toString()));
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/rates")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.
                andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId", CoreMatchers.is(dto.getCurrencyId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyName", CoreMatchers.is(dto.getCurrencyName())));
    }

    @Test
    @DisplayName("Test handle InvalidDateFormatException  functionality")
    public void givenIncorrectDate_whenSaveRate_thenMessageError() throws Exception {
        //when
        var message = messageSource.getMessage("errors.date.empty", new Object[0], new Locale("en"));
        BDDMockito.given(rateService.saveAllRatesByDateFromExternalApi(any()))
                .willThrow(new InvalidDateFormatException("errors.date.empty"));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/rates/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RateLoadingRequestDto(null))));

        //then
        result.
                andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));

    }
}
