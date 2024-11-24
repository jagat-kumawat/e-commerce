package com.ecommerce.api.controller.auth;
import com.ecommerce.api.model.RegistrationBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot","secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private MockMvc mvc;

@Test
@Transactional
    public void testRegister() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    RegistrationBody body = new RegistrationBody();

body.setEmail("authenticationcontrollertest$testregister@junit.com");
body.setFirstname("firstname1");
body.setLastname("lastname1");
body.setPassword("Jagat12345");
    body.setUsername(null);
    mvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    body.setUsername("");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    body.setUsername("AuthenticationControllerTest$testRegister");

    body.setEmail(null);
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));


    body.setEmail("");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    body.setEmail("AuthenticationControllerTest$testRegister@junit.com");


    body.setPassword(null);
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    body.setPassword("");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));




    body.setPassword("Jagat12345");
    body.setFirstname(null);
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    body.setFirstname("");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));




    body.setFirstname("firstname1");
    body.setLastname(null);
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    body.setLastname("");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    body.setLastname("lastname1");
    mvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body)))
            .andExpect(status().is(HttpStatus.OK.value()));

}

}
