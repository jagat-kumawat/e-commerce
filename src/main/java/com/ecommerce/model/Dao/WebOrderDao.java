package com.ecommerce.model.Dao;

import com.ecommerce.model.WebOrder;
import com.ecommerce.model.localuser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDao extends ListCrudRepository<WebOrder,Long> {
    List<WebOrder> findByUser(localuser user);
}
