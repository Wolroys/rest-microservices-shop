package com.wolroys.shopentity.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    private String username;

    @Min(value = 6, message = "Your password can not be at least than 6 symbols")
    private String password;
}
