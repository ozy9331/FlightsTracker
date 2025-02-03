package io.vitech.flights.tracker.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
