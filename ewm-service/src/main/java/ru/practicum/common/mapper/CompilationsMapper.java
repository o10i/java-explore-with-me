package ru.practicum.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.dto.NewCompilationDto;
import ru.practicum.common.model.Compilation;

import static ru.practicum.common.mapper.EventMapper.toEventShortDtoList;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationsMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                toEventShortDtoList(compilation.getEvents()),
                compilation.getPinned(),
                compilation.getTitle());
    }
}
