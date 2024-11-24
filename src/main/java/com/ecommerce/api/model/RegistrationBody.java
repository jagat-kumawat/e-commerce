package com.ecommerce.api.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegistrationBody {

    @NotBlank
     @Size(min =3,max = 255)
   private String username;



        @NotNull
        @Email
        private String email;

 @NotBlank
    @Size(min =5,max = 15)
    @Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{3,}$")
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;
}
