package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDao extends MongoRepository<Login,String>{
    Optional<Login> findByRefreshToken(String refreshToken);
    Optional<Login> findByUsername(String username);

}
