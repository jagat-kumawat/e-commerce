package com.ecommerce.api.controller;

import com.ecommerce.Exception.EmailFailureException;
import com.ecommerce.Exception.EmailNotFoundException;
import com.ecommerce.Exception.UserNotVerifiedException;
import com.ecommerce.Exception.userAlreadyExsistsException;
import com.ecommerce.api.model.JWTbody;
import com.ecommerce.api.model.Jwtresponse;
import com.ecommerce.api.model.PasswordResetBody;
import com.ecommerce.api.model.RegistrationBody;
import com.ecommerce.model.localuser;
import com.ecommerce.services.userService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private userService userser;

    @PostMapping("/register")
    public ResponseEntity<?> registrationbody(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            try {
                userser.registration(registrationBody);
            } catch (EmailFailureException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (userAlreadyExsistsException e) {
            return new ResponseEntity(e, HttpStatus.CONFLICT);

        }


    }

    @PostMapping("/login")
    public ResponseEntity<Jwtresponse> loginuserjwt(@Valid @RequestBody JWTbody jwTbody) {
        String loginjwt = null;
        try {
            loginjwt = userser.loginjwt(jwTbody);
        } catch (UserNotVerifiedException e) {
            Jwtresponse jwtresponse = new Jwtresponse();
            jwtresponse.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";


            if (e.isNewEmailSent()) {
                reason += "EMAIL_RESENT";
            }
            jwtresponse.setFailureReson(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(jwtresponse);

        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (loginjwt == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Jwtresponse jwtResponse = new Jwtresponse();
            jwtResponse.setJwt(loginjwt);
            jwtResponse.setSuccess(true);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        if (userser.verifyuser(token)) {
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @GetMapping("/me")
    public localuser getLoggedInUserProfile(@AuthenticationPrincipal localuser user) {
        return user;
    }


    @PostMapping("/forgot")
    public ResponseEntity forgotPassswordController(@RequestParam String email) {
        try {
            userser.forgotPassword(email);
        return ResponseEntity.ok().build();

        } catch (EmailNotFoundException e) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody body){
      String s = userser.resetpassword(body);
        return  new ResponseEntity(HttpStatus.OK);

    }
}