package com.ecommerce.api.security;

import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.localuser;
import com.ecommerce.services.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private LocalUserDao localUserDao;
    private static final  String AUTHENTICATED_PATH = "/auth/me";  ///auth/me

    @Test
    public void testunauthicatedRequest()throws Exception{
        mvc.perform(get(AUTHENTICATED_PATH)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }
    @Test
    public void testBedToken() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization","BadTokenThatisnotvalid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization","Bearer BadTokenThatisnotvalid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }
    @Test
    public void testUnverifiedUser() throws Exception {
        localuser user = localUserDao.findByUsernameIgnoreCase("test-5").get();
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization","Bearer "+token)) .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
    @Test

    public void testSuccessfully() throws Exception {
        localuser user = localUserDao.findByUsernameIgnoreCase("test-6").get();
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization","Bearer "+token)) .andExpect(status()
                .is(HttpStatus.OK.value()));

    }
}
