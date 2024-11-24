package com.ecommerce.model.Dao;

import com.ecommerce.model.product;
import org.springframework.data.repository.ListCrudRepository;

public interface productDao extends ListCrudRepository<product, Long> {

}
