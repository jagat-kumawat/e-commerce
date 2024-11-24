package com.ecommerce.model.Dao;

import com.ecommerce.model.VerificationToken;
import com.ecommerce.model.localuser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository

public interface VerificationTokenDao extends CrudRepository<VerificationToken,Long> {

    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(localuser user);
    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);
}
