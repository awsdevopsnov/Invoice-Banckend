package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends MongoRepository<User,String> {
    Optional<User> findById(String id);
    Optional<User> findByUserEmail(String userEmail);

}
