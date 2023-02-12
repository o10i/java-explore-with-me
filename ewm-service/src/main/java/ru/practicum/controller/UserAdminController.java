package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService service;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getAll(ids, from, size), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDto> save(@RequestBody @Valid NewUserRequest newUserRequest) {
        return new ResponseEntity<>(service.save(newUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        service.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
