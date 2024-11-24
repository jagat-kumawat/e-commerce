package com.ecommerce.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Jwtresponse {
    private String jwt;
    private boolean success;
    private String failureReson;

    public Jwtresponse() {

    }
}
