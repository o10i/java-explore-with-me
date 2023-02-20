package ru.practicum.APIadmin.service;

import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.dto.NewCompilationDto;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, NewCompilationDto newCompilationDto);
}
