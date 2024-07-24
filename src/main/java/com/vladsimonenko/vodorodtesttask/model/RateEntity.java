package com.vladsimonenko.vodorodtesttask.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Entity
@Table(name = "t_rates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency_id")
    private Integer currencyId;

    private Date date;

    @Column(name = "currency_abbreviation")
    private String currencyAbbreviation;

    @Column(name = "currency_scale")
    private Integer currencyScale;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "currency_official_rate")
    private Double currencyOfficialRate;
}
