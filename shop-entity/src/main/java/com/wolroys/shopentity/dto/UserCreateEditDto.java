package com.wolroys.shopentity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateEditDto {

    @NotNull(message = "Specify your username")
    @NotBlank(message = "Specify your username")
    private String username;

    @Min(value = 6, message = "Your password can not be at least than 6 symbols")
    private String password;

    @Email(message = "Specify correct email")
    private String email;
}
