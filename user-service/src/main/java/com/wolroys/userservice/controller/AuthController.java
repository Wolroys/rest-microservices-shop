package com.wolroys.userservice.controller;

import com.wolroys.shopentity.dto.AuthDto;
import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserCreateEditDto user){
        if (userService.isExist(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This user already exists");

        String token = userService.register(user);

        return ResponseEntity.ok("token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto authDto){
        if (!userService.isExist(authDto.getUsername()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account doesn't exist");

       return ResponseEntity.ok(userService.login(authDto));
    }
}
