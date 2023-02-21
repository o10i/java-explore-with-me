package ru.practicum.ewm.APIadmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.APIadmin.service.AdminCategoryService;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService service;

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
