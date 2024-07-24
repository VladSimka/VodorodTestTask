package com.vladsimonenko.vodorodtesttask.service;

import com.vladsimonenko.vodorodtesttask.client.RateClient;
import com.vladsimonenko.vodorodtesttask.dto.RateExternalApiDto;
import com.vladsimonenko.vodorodtesttask.dto.RateLoadingRequestDto;
import com.vladsimonenko.vodorodtesttask.exception.InvalidDateFormatException;
import com.vladsimonenko.vodorodtesttask.exception.InvalidParameterException;
import com.vladsimonenko.vodorodtesttask.exception.RatesAlreadyExistsException;
import com.vladsimonenko.vodorodtesttask.mapper.RateMapper;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import com.vladsimonenko.vodorodtesttask.repository.RateRepository;
import com.vladsimonenko.vodorodtesttask.service.impl.RateServiceImpl;
import com.vladsimonenko.vodorodtesttask.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RateServiceImplTests {

    @Mock
    private RateRepository rateRepository;

    @Mock
    private RateClient rateClient;

    @Mock
    private RateMapper rateMapper;

    @InjectMocks
    private RateServiceImpl rateService;

    @Test
    @DisplayName("Test save all rates by date from external api")
    public void givenRequestDto_whenSaveAllRates_thenSuccess() {
        //given
        var firstApi = DataUtils.getFirstRateApi();
        var secondApi = DataUtils.getSecondRateApi();
        var thirdApi = DataUtils.getThirdRateApi();
        var first = DataUtils.getFirstRateTransient();
        var second = DataUtils.getSecondRateTransient();
        var third = DataUtils.getThirdRateTransient();
        var firstRatePersisted = DataUtils.getFirstRatePersisted();
        var secondRatePersisted = DataUtils.getSecondRatePersisted();
        var thirdRatePersisted = DataUtils.getThirdRatePersisted();
        var request = new RateLoadingRequestDto(first.getDate().toString());
        List<RateExternalApiDto> externalApiDtos = List.of(firstApi, secondApi, thirdApi);
        List<RateEntity> entities = List.of(first, second, third);
        List<RateEntity> persisted = List.of(firstRatePersisted, secondRatePersisted, thirdRatePersisted);
        BDDMockito.given(rateClient.getAllRatesByDate(any(Date.class), anyInt()))
                .willReturn(externalApiDtos);
        BDDMockito.given(rateRepository.existsByDate(any(Date.class)))
                .willReturn(false);
        BDDMockito.given(rateRepository.saveAll(any())).willReturn(persisted);
        BDDMockito.given(rateMapper.toEntity(anyList()))
                .willReturn(entities);

        //when
        List<RateEntity> entityList = rateService.saveAllRatesByDateFromExternalApi(request);

        //then
        assertThat(entityList).isNotNull();
        assertThat(entityList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test save all rates by date from external api secondTime")
    public void givenRequestDto_whenSaveAllRates_thenExceptionThrown() {
        //given
        var first = DataUtils.getFirstRateTransient();
        var request = new RateLoadingRequestDto(first.getDate().toString());
        BDDMockito.given(rateRepository.existsByDate(any(Date.class)))
                .willReturn(true);

        //when
        assertThrows(
                RatesAlreadyExistsException.class, () -> rateService.saveAllRatesByDateFromExternalApi(request));

        //then
        verify(rateClient, never()).getAllRatesByDate(any(), anyInt());
        verify(rateMapper, never()).toEntity(anyList());
        verify(rateRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Test save all rates by null date ")
    public void givenNullDate_whenSaveAllRates_thenExceptionThrown() {
        //given
        var request = new RateLoadingRequestDto(null);

        //when
        assertThrows(
                InvalidDateFormatException.class, () -> rateService.saveAllRatesByDateFromExternalApi(request));

        //then
        verify(rateRepository, never()).existsByDate(any(Date.class));
        verify(rateClient, never()).getAllRatesByDate(any(), anyInt());
        verify(rateMapper, never()).toEntity(anyList());
        verify(rateRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Test save all rates by incorrect format date ")
    public void givenIncorrectDate_whenSaveAllRates_thenExceptionThrown() {
        //given
        var request = new RateLoadingRequestDto("incorrect");

        //when
        assertThrows(
                InvalidDateFormatException.class, () -> rateService.saveAllRatesByDateFromExternalApi(request));

        //then
        verify(rateRepository, never()).existsByDate(any(Date.class));
        verify(rateClient, never()).getAllRatesByDate(any(), anyInt());
        verify(rateMapper, never()).toEntity(anyList());
        verify(rateRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Test get developer by currency id and date functionality when exist in db")
    public void givenCurrencyIdAndDate_whenFindRate_thenRateReturnedFromDb() {
        //given
        var first = DataUtils.getFirstRateTransient();
        var firstPersisted = DataUtils.getFirstRatePersisted();
        Integer currencyId = first.getCurrencyId();
        Date date = first.getDate();
        BDDMockito.given(rateRepository.findByCurrencyIdAndDate(anyInt(), any(Date.class)))
                .willReturn(Optional.of(firstPersisted));

        //when
        RateEntity rate = rateService.getRateByDateAndCode(date.toString(), currencyId, null);

        //then
        assertThat(rate).isNotNull();
        assertThat(rate.getDate()).isEqualTo(date);
        assertThat(rate.getCurrencyId()).isEqualTo(currencyId);
        verify(rateClient, never()).getRateByDateAndParameter(any(), any(), any());
    }

    @Test
    @DisplayName("Test get developer by currency id and date functionality when doesn't exist in db")
    public void givenCurrencyIdAndDate_whenFindRate_thenRateReturned() {
        //given
        var first = DataUtils.getFirstRateTransient();
        var firstApi = DataUtils.getFirstRateApi();
        Integer currencyId = first.getCurrencyId();
        Date date = first.getDate();
        BDDMockito.given(rateRepository.findByCurrencyIdAndDate(anyInt(), any(Date.class)))
                .willReturn(Optional.empty());
        BDDMockito.given(rateClient.getRateByDateAndParameter(any(), anyInt(), any()))
                .willReturn(firstApi);
        BDDMockito.given(rateMapper.toEntity(any(RateExternalApiDto.class)))
                .willReturn(first);

        //when
        RateEntity rate = rateService.getRateByDateAndCode(date.toString(), currencyId, null);

        //then
        assertThat(rate).isNotNull();
        assertThat(rate.getDate()).isEqualTo(date);
        assertThat(rate.getCurrencyId()).isEqualTo(currencyId);
    }

    @Test
    @DisplayName("Test get developer by currency abbreviation and date functionality when exist in db")
    public void givenCurrencyAbbrAndDate_whenFindRate_thenRateReturnedFromDb() {
        //given
        var first = DataUtils.getFirstRateTransient();
        var firstPersisted = DataUtils.getFirstRatePersisted();
        String abbreviation = first.getCurrencyAbbreviation();
        Date date = first.getDate();
        BDDMockito.given(rateRepository.findByCurrencyAbbreviationAndDate(anyString(), any(Date.class)))
                .willReturn(Optional.of(firstPersisted));

        //when
        RateEntity rate = rateService.getRateByDateAndCode(date.toString(), null, abbreviation);

        //then
        assertThat(rate).isNotNull();
        assertThat(rate.getDate()).isEqualTo(date);
        assertThat(rate.getCurrencyAbbreviation()).isEqualTo(abbreviation);
        verify(rateClient, never()).getRateByDateAndParameter(any(), any(), any());
    }

    @Test
    @DisplayName("Test get developer by currency abbreviation and date functionality when doesn't exist in db")
    public void givenCurrencyAbbrAndDate_whenFindRate_thenRateReturned() {
        //given
        var first = DataUtils.getFirstRateTransient();
        var firstApi = DataUtils.getFirstRateApi();
        String abbreviation = first.getCurrencyAbbreviation();
        Date date = first.getDate();
        BDDMockito.given(rateRepository.findByCurrencyAbbreviationAndDate(anyString(), any(Date.class)))
                .willReturn(Optional.empty());
        BDDMockito.given(rateClient.getRateByDateAndParameter(any(), anyInt(), any()))
                .willReturn(firstApi);
        BDDMockito.given(rateMapper.toEntity(any(RateExternalApiDto.class)))
                .willReturn(first);

        //when
        RateEntity rate = rateService.getRateByDateAndCode(date.toString(), null, abbreviation);

        //then
        assertThat(rate).isNotNull();
        assertThat(rate.getDate()).isEqualTo(date);
        assertThat(rate.getCurrencyAbbreviation()).isEqualTo(abbreviation);
    }

    @Test
    @DisplayName("Test get rate by null params functionality")
    public void givenNullIdAnAbbr_whenFindRate_thenExceptionThrown() {
        //given
        var first = DataUtils.getFirstRateTransient();

        //when
        assertThrows(
                InvalidParameterException.class, () -> rateService.getRateByDateAndCode(String.valueOf(first.getDate()), null, null)
        );

        //then
        verify(rateClient, never()).getRateByDateAndParameter(any(), any(), any());
        verify(rateMapper, never()).toEntity(any(RateExternalApiDto.class));
    }
}
