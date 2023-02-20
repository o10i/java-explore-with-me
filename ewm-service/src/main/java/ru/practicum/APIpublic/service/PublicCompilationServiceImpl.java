package ru.practicum.APIpublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.model.Compilation;
import ru.practicum.common.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.common.mapper.CompilationMapper.toCompilationDtoList;

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
