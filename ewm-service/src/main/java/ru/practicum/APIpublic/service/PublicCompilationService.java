package ru.practicum.APIpublic.service;

import ru.practicum.common.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);
}
