package com.ecommerce.services;

import com.ecommerce.model.Dao.productDao;
import com.ecommerce.model.product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
@AllArgsConstructor
public class productService {

private productDao pro;

public List<product> getproduct(){
    return pro.findAll();
}

}
