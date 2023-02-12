package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryRequestDto;
import ru.practicum.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService service;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody CategoryRequestDto categoryRequestDto) {
        return new ResponseEntity<>(service.save(categoryRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        service.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> update(@RequestBody CategoryRequestDto categoryRequestDto,
                                         @PathVariable Long catId) {
        return ResponseEntity.ok(service.update(categoryRequestDto, catId));
    }
}
