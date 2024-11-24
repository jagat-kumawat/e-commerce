package com.ecommerce.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordResetBody {
    @NotBlank
    @NotNull
    private String token;
    @NotBlank
    @NotNull
    @Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{3,}$")
    @Size(min = 6,max = 32)
    private String password;

}
