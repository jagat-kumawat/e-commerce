package com.ecommerce.api.controller.order;

import com.ecommerce.model.WebOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class orderControllerTest {
    @Autowired   MockMvc mvc;

    @Test
//    @WithUserDetails("test-5")
    public void testAauthenticatedOrderList() throws Exception {
        testauthenticatedOrderListbelongsTouser("test-5");


    }

    @Test
//    @WithUserDetails("test-5")
    public void testbauthenticatedOrderList() throws Exception {
        testauthenticatedOrderListbelongsTouser("test-6");

    }

    public void testauthenticatedOrderListbelongsTouser(String username) throws Exception {
        mvc.perform(get("/order/s")).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<WebOrder> order = new ObjectMapper().readValue(json, new TypeReference<List<WebOrder>>() {
                    });
                    for (WebOrder order1 : order){
                        Assertions.assertEquals(username,order1.getUser().getUsername(),"order list should only be orders belonging to the user");
                    }
                });
    }



    @Test
    public void testUnauthenticatedOrderList() throws Exception {
        mvc.perform(get("/order/s")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}
