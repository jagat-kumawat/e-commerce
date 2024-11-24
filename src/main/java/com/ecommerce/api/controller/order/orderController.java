package com.ecommerce.api.controller.order;

import com.ecommerce.model.WebOrder;
import com.ecommerce.model.localuser;
import com.ecommerce.services.WebOrderservice;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class orderController {
    private WebOrderservice webOrderservice;
    @GetMapping("/s")
    public List<WebOrder> getaorder(@AuthenticationPrincipal localuser user){

        return  webOrderservice.getuser(user);
    }

}
