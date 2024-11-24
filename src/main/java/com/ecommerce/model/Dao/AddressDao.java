package com.ecommerce.model.Dao;

import com.ecommerce.model.Address;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDao extends ListCrudRepository<Address, Long> {
    List<Address> findByUser_Id(Long userId);
}
