package com.vladsimonenko.vodorodtesttask.repository;

import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import com.vladsimonenko.vodorodtesttask.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RateRepositoryTests {

    @Autowired
    private RateRepository rateRepository;

    @BeforeEach
    public void setUp() {
        rateRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save rate functionality")
    public void givenRate_whenSaveRate_thenRateSaved() {
        //given
        RateEntity rateToSave = DataUtils.getFirstRateTransient();

        //when
        RateEntity saved = rateRepository.save(rateToSave);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test save rates functionality")
    public void givenThreeRates_whenSaveRates_thenRatesSaved() {
        //given
        RateEntity firstRate = DataUtils.getFirstRateTransient();
        RateEntity secondRate = DataUtils.getSecondRateTransient();
        RateEntity thirdRate = DataUtils.getThirdRateTransient();
        List<RateEntity> rates = List.of(firstRate, secondRate, thirdRate);

        //when
        List<RateEntity> saved = rateRepository.saveAll(rates);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.size()).isEqualTo(3);
        assertThat(saved.get(0).getId()).isNotNull();
    }

    @Test
    @DisplayName("Test find rate by currency id and date")
    public void givenCreatedRate_whenFindRateById_thenRateFound() {
        //given
        RateEntity rateToSave = DataUtils.getFirstRateTransient();
        rateRepository.save(rateToSave);

        //when
        Optional<RateEntity> obtainedRate =
                rateRepository.findByCurrencyIdAndDate(rateToSave.getCurrencyId(), rateToSave.getDate());

        //then
        assertThat(obtainedRate.isPresent()).isTrue();
        assertThat(obtainedRate.get().getCurrencyName()).isEqualTo(rateToSave.getCurrencyName());
    }

    @Test
    @DisplayName("Test find rate by incorrect currency id and date")
    public void givenRateIsNotCreated_whenFindRateById_thenOptionalIsEmpty() {
        //given
        RateEntity rate = DataUtils.getFirstRateTransient();

        //when
        Optional<RateEntity> obtainedRate =
                rateRepository.findByCurrencyIdAndDate(rate.getCurrencyId(), rate.getDate());

        //then
        assertThat(obtainedRate.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test find rate by currency abbreviation and date")
    public void givenCreatedRate_whenFindRateByAbbreviation_thenRateFound() {
        //given
        RateEntity rateToSave = DataUtils.getFirstRateTransient();
        rateRepository.save(rateToSave);

        //when
        Optional<RateEntity> obtainedRate =
                rateRepository.findByCurrencyAbbreviationAndDate(rateToSave.getCurrencyAbbreviation(), rateToSave.getDate());

        //then
        assertThat(obtainedRate.isPresent()).isTrue();
        assertThat(obtainedRate.get().getCurrencyName()).isEqualTo(rateToSave.getCurrencyName());
    }

    @Test
    @DisplayName("Test find rate by incorrect currency abbreviation and date")
    public void givenRateIsNotCreated_whenFindRateByAbbreviation_thenOptionalIsEmpty() {
        //given
        RateEntity rate = DataUtils.getFirstRateTransient();

        //when
        Optional<RateEntity> obtainedRate =
                rateRepository.findByCurrencyAbbreviationAndDate(rate.getCurrencyAbbreviation(), rate.getDate());

        //then
        assertThat(obtainedRate.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test rate exists by date functionality")
    public void givenCreatedRate_whenExistsRateByDate_thenTrueReturned() {
        //given
        RateEntity rateToSave = DataUtils.getFirstRateTransient();
        rateRepository.save(rateToSave);

        //when
        boolean existsByDate = rateRepository.existsByDate(rateToSave.getDate());

        //then
        assertThat(existsByDate).isTrue();
    }

    @Test
    @DisplayName("Test rate exists by incorrect date functionality")
    public void givenRateIsNotCreated_whenExistsRateByDate_thenFalseReturned() {
        //given
        RateEntity rateToSave = DataUtils.getFirstRateTransient();


        //when
        boolean existsByDate = rateRepository.existsByDate(rateToSave.getDate());

        //then
        assertThat(existsByDate).isFalse();
    }
}
