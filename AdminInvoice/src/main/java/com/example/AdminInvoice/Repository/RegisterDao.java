package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.Register;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegisterDao extends MongoRepository<Register,String> {
    Optional<Register> findByUsername(String username);
    Optional<Register> findByUserEmail(String userEmail);

}
