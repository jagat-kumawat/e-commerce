package com.ecommerce.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.localuser;
import com.ecommerce.services.JWTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTServiceTest {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey; // secert
@Autowired
    private JWTService jwtService;
@Autowired
    private LocalUserDao localUserDao;
@Test
    public void testVerificationTokenusableforlogin(){
    localuser user  = localUserDao.findByUsernameIgnoreCase("test-5").get();
    String token = jwtService.generateverificationjwt(user);
    Assertions.assertNull(jwtService.getusername(token),"verification token should not contain username");

}
@Test
    public void testtokenReturnusername(){
    localuser user = localUserDao.findByUsernameIgnoreCase("test-5").get();
    String token = jwtService.generateJWT(user);
    Assertions.assertEquals(user.getUsername(),jwtService.getusername(token),"token for auth should contain user username");
}
@Test
    public void testJWTNotGeneratedByuser() {
    String token =
    JWT.create().withClaim("USERNAME","test-5").sign(Algorithm.HMAC256(
            "NotTHeRealSecret"
    ));
    Assertions.assertThrows(SignatureVerificationException.class,() -> jwtService.getusername(token));

}
@Test
    public void  testJwtCorrectlySignedNotIssuer(){
    String token = JWT.create().withClaim("USERNAME","test-5").sign(Algorithm.HMAC256(algorithmKey));
    Assertions.assertThrows(MissingClaimException.class ,() -> jwtService.getusername(token));

}



    @Test
    public void testResetPasswordJWTNotGeneratedByuser() {
        String token =
                JWT.create().withClaim(
                        "Reset_password_key","user1@junit.com").sign(Algorithm.HMAC256(
                        "NotTHeRealSecret"
                ));
        Assertions.assertThrows(SignatureVerificationException.class,() -> jwtService.getResetpasswordemail(token));

    }
    @Test
    public void  testResetPasswordJwtCorrectlySignedNotIssuer(){
        String token = JWT.create().withClaim( "Reset_password_key","user1@junit.com").sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class ,() -> jwtService.getResetpasswordemail(token));

    }




@Test
    public void testPasswordResetToken (){
    localuser user = localUserDao.findByUsernameIgnoreCase("test-5").get();
    String token  = jwtService.generateResetVerificationPasswordjwt(user);
    Assertions.assertEquals(user.getEmail(),jwtService.getResetpasswordemail(token),"email should match inside "+"JWT");
}

}
