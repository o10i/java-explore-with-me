package ru.practicum.ewm.apiAdmin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;
import ru.practicum.ewm.common.dto.UpdateCompilationRequest;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.repository.EventRepository;

import static ru.practicum.ewm.common.mapper.CompilationMapper.toCompilation;
import static ru.practicum.ewm.common.mapper.CompilationMapper.toCompilationDto;

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
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getByIdWithCheck(compId);
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
        }
        return toCompilationDto(repository.save(compilation));
    }

    public Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
