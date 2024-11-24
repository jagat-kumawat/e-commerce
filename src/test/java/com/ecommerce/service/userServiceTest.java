package com.ecommerce.service;

import com.ecommerce.Exception.EmailFailureException;
import com.ecommerce.Exception.UserNotVerifiedException;
import com.ecommerce.Exception.userAlreadyExsistsException;
import com.ecommerce.api.model.JWTbody;
import com.ecommerce.api.model.PasswordResetBody;
import com.ecommerce.api.model.RegistrationBody;
import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.Dao.VerificationTokenDao;
import com.ecommerce.model.VerificationToken;
import com.ecommerce.model.localuser;
import com.ecommerce.services.EncryptionService;
import com.ecommerce.services.JWTService;
import com.ecommerce.services.userService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class userServiceTest {
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private userService userser;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private VerificationTokenDao verdao;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private LocalUserDao localUserDao;

    @Test
    @Transactional
    public void testRegisteruser() throws MessagingException {
        RegistrationBody re = new RegistrationBody();
        re.setUsername("test-10");
        re.setEmail("test10@gmail.com");
        re.setFirstname("vinod");
        re.setLastname("kumar");
        re.setPassword("Jatin12345");
        Assertions.assertThrows(userAlreadyExsistsException.class, () -> userser.registration(re), "user should already be in use");
        re.setUsername("test-10");
        re.setEmail("test10@gmail.com");
        Assertions.assertThrows(userAlreadyExsistsException.class, () -> userser.registration(re), "Email should already be in use");
        re.setEmail("test10@gmail.com");

        Assertions.assertDoesNotThrow(() -> userser.registration(re), "user should register successfully");
        Assertions.assertEquals(re.getEmail(), greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());

    }

    @Test
    @Transactional
    public void testloginUser() throws UserNotVerifiedException, EmailFailureException {
        JWTbody jw = new JWTbody();
        jw.setUsername("user1-notExists");
        jw.setPassword("Jagat12345-BadPassword");
        Assertions.assertNull(userser.loginjwt(jw), "the user should not exists");
        jw.setUsername("test-10");
        Assertions.assertNull(userser.loginjwt(jw), "the password should be increct");
        jw.setPassword("Jatin12345");
        Assertions.assertNull(userser.loginjwt(jw), "the user should be login successfully");
        jw.setUsername("test-10");
        jw.setPassword("Jatin12345");
        try {
            userser.loginjwt(jw);
            Assertions.assertTrue(false, "user should not have email verfied");

        } catch (UserNotVerifiedException ex) {
            Assertions.assertTrue(ex.isNewEmailSent(), "Email verfication should be sent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try {
            userser.loginjwt(jw);
            Assertions.assertTrue(false, "user should not have email verfied");

        } catch (UserNotVerifiedException ex) {
            Assertions.assertFalse(ex.isNewEmailSent(), "Email verfication should not be resent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }

    }

    @Test
    @Transactional
    public void testverifyuser() throws EmailFailureException {
        Assertions.assertFalse(userser.verifyuser("bad token"), "token this is bad or does not exists should return false");
        JWTbody body = new JWTbody();
        body.setUsername("test-10");
        body.setPassword("$2a$10$XbQ/67sC.UwCrVttq8o0Se/MEBcqBvHzgduPu8gOh97Sf54umOvq.");
        try {
            userser.loginjwt(body);
            Assertions.assertTrue(false, "user should not have email verified");

        } catch (UserNotVerifiedException e) {
            List<VerificationToken> tokens = verdao.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userser.verifyuser(token), "token should be valid");
            Assertions.assertNotNull(body, "the user should now be verified ");
        }

    }
    @Test
    @Transactional
    public void testforgotpassword() throws MessagingException {
        Assertions.assertThrows(EmailFailureException.class,()-> userser.forgotPassword("userNotExist@junit.com"));
        Assertions.assertDoesNotThrow(()->userser.forgotPassword("test10@gmail.com"),"non existing email should be rejected");
        Assertions.assertEquals("test10@gmail.com",
                greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0]
                        .toString(),"password "+"reset email should be sent");
    }
@Test
@Transactional
    public void testRestPassword(){
        localuser user = localUserDao.findByUsernameIgnoreCase("test-10").get();
        String token  = jwtService.generateResetVerificationPasswordjwt(user);
        PasswordResetBody body = new PasswordResetBody();
        body.setToken(token);
        body.setPassword("Yogest@12345");
        userser.resetpassword(body);
        user = localUserDao.findByUsernameIgnoreCase("test-10").get();
        Assertions.assertTrue(encryptionService.verifypassword("Yogesh@12345",user.getPassword()),"password change should be written ro database");

    }

}