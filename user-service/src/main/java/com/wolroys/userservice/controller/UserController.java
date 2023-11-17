package com.wolroys.userservice.controller;

import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getProfileById(@PathVariable Long id){
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registration(@RequestBody UserCreateEditDto userDto){
        return userService.create(userDto);
    }

    @PatchMapping("/edit/{id}")
    public UserDto edit(@PathVariable Long id, @RequestBody UserCreateEditDto userDto){
        return userService.update(id, userDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        if (!userService.delete(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
