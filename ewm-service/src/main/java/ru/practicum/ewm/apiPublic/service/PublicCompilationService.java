package ru.practicum.ewm.apiPublic.service;

import ru.practicum.ewm.common.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);
}
