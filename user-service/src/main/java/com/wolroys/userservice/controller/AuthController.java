package com.wolroys.userservice.controller;

import com.wolroys.shopentity.dto.AuthDto;
import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.userservice.service.EmailServiceImpl;
import com.wolroys.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailServiceImpl emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserCreateEditDto user){
        if (userService.isExist(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This account already exists");

        if (userService.isTaken(user.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This email has already been registered");

        String token = userService.register(user);

        return ResponseEntity.ok("You need to confirm your account. Check your email \ntoken: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto authDto){
        if (!userService.isExist(authDto.getUsername())
                && userService.findByUsername(authDto.getUsername()).get().getActivationCode() != null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account doesn't exist");

       return ResponseEntity.ok("token: " + userService.login(authDto));
    }

    @GetMapping("/activate/{code}")
    @ResponseBody
    public ResponseEntity<?> activate(@PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if (isActivated)
            return new ResponseEntity<>("User successfully activated", HttpStatus.CREATED);

        return new ResponseEntity<>("Activation code isn't found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/simple-email/{user-email}")
    public @ResponseBody ResponseEntity<?> sendSimpleEmail(@PathVariable("user-email") String email){
        try{
            emailService.sendSimpleEmail(email, "Welcome", "This is a welcome email for you!");
        } catch (MailException e){
            e.printStackTrace();
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
    }


}
