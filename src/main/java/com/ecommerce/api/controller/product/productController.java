package com.ecommerce.api.controller.product;

import com.ecommerce.model.product;
import com.ecommerce.services.productService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class productController {
    private productService prdt;
    @GetMapping("/s")
    public List<product> getallproduct(){
        return prdt.getproduct();
    }
}
