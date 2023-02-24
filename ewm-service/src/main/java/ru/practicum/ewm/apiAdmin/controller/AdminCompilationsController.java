package ru.practicum.ewm.apiAdmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apiAdmin.service.AdminCompilationService;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;
import ru.practicum.ewm.common.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService service;

    @PostMapping()
    public ResponseEntity<CompilationDto> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(service.save(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        service.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return ResponseEntity.ok(service.update(compId, updateCompilationRequest));
    }
}
