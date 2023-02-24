package ru.practicum.ewm.apiPublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.ewm.common.mapper.CompilationMapper.toCompilationDtoList;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository repository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = repository.findAllByPinned(pinned);
        } else {
            compilations = repository.findAll();
        }
        return toCompilationDtoList(compilations.stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Override
    public CompilationDto getById(Long compId) {
        return toCompilationDto(getByIdWithCheck(compId));
    }

    public Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
