package com.ecommerce.model.Dao;

import com.ecommerce.model.localuser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalUserDao extends ListCrudRepository<localuser,Long> {

    Optional<localuser> findByUsernameIgnoreCase(String username);
    Optional<localuser> findByEmailIgnoreCase(String email);


}
