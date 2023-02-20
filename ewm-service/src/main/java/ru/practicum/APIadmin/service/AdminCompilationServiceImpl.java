package ru.practicum.APIadmin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.dto.NewCompilationDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.model.Compilation;
import ru.practicum.common.repository.CompilationRepository;
import ru.practicum.common.repository.EventRepository;

import static ru.practicum.common.mapper.CompilationsMapper.toCompilation;
import static ru.practicum.common.mapper.CompilationsMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    CompilationRepository repository;
    EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));

        return toCompilationDto(repository.save(compilation));
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        repository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = getByIdWithCheck(compId);
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        }
        return toCompilationDto(repository.save(compilation));
    }

    public Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
