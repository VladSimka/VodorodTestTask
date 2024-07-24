package com.vladsimonenko.vodorodtesttask.client;

import com.vladsimonenko.vodorodtesttask.dto.RateExternalApiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@FeignClient(name = "rate", url = "https://api.nbrb.by")
public interface RateClient {

    @GetMapping("/exrates/rates")
    List<RateExternalApiDto> getAllRatesByDate(@RequestParam("ondate") Date date,
                                               @RequestParam("periodicity") Integer paramMode);


    @GetMapping("/exrates/rates/{cur_id}")
    RateExternalApiDto getRateByDateAndParameter(@PathVariable("cur_id") String cur,
                                                 @RequestParam("parammode") Integer paramMode,
                                                 @RequestParam("ondate") Date date);
}
