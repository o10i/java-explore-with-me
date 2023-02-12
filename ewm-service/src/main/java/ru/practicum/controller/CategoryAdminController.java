package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService service;

    @PostMapping()
    public ResponseEntity<CategoryDto> save(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(service.save(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        service.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody NewCategoryDto newCategoryDto,
                                         @PathVariable Long catId) {
        return ResponseEntity.ok(service.update(newCategoryDto, catId));
    }
}
