package ru.practicum.ewm.apiAdmin.service;

import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;
import ru.practicum.ewm.common.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
