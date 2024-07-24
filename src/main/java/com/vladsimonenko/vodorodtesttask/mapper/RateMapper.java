package com.vladsimonenko.vodorodtesttask.mapper;

import com.vladsimonenko.vodorodtesttask.dto.RateExternalApiDto;
import com.vladsimonenko.vodorodtesttask.dto.RateResponseDto;
import com.vladsimonenko.vodorodtesttask.model.RateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RateMapper {

    List<RateEntity> toEntity(List<RateExternalApiDto> dtos);

    RateEntity toEntity(RateExternalApiDto dto);

    RateResponseDto toDto(RateEntity entity);
}
