CREATE TABLE IF NOT EXISTS t_rates
(
    id                   INT PRIMARY KEY,
    currency_id           INT,
    date                 DATE,
    currency_abbreviation VARCHAR(255),
    currency_scale        INT,
    currency_name         VARCHAR(255),
    currency_official_rate DOUBLE
);