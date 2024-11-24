package com.ecommerce.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.model.localuser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service

public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey; // secert
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryInSeconds}")
    private int expiryInSecond;
 private Algorithm algorithm;
 private static final  String USERNAME_KEY="USERNAME";
 private static final  String Email_KEY="EMAIL";
 private static final String Reset_password_key = "RESETPASSWORD";


 @PostConstruct
    public void postconstruct(){
     algorithm=Algorithm.HMAC256(algorithmKey);


 }
    public String generateJWT(localuser user){
        return JWT.create().withClaim(USERNAME_KEY,user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*expiryInSecond)))
                .withIssuer(issuer).sign(algorithm);

    }
    public String generateverificationjwt(localuser user){
     return JWT.create().withClaim(Email_KEY,user.getEmail())
             .withExpiresAt(new Date(System.currentTimeMillis()+(1000*expiryInSecond)))
             .withIssuer(issuer).sign(algorithm);
    }
    public String generateResetVerificationPasswordjwt(localuser user){
        return JWT.create().withClaim(Reset_password_key,user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*60*30)))
                .withIssuer(issuer).sign(algorithm);
    }
    public String getResetpasswordemail(String token){
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return jwt.getClaim(Reset_password_key).asString();


    }


    public  String getusername(String token){
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
     return jwt.getClaim(USERNAME_KEY).asString();
    }



}
