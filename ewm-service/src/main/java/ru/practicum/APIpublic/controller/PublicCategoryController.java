package ru.practicum.APIpublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.APIpublic.service.PublicCategoryService;
import ru.practicum.common.dto.CategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final PublicCategoryService service;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAll(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        return ResponseEntity.ok(service.getById(catId));
    }
}
