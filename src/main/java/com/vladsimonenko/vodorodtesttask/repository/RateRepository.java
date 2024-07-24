package com.vladsimonenko.vodorodtesttask.repository;

import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface RateRepository extends JpaRepository<RateEntity, Integer> {

    Optional<RateEntity> findByCurrencyIdAndDate(Integer currencyId, Date date);

    Optional<RateEntity> findByCurrencyAbbreviationAndDate(String currencyAbbreviation, Date date);

    boolean existsByDate(Date date);
}
