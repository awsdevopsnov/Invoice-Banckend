package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.SettingsPortals;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingPortalsDao extends MongoRepository<SettingsPortals,String> {
    Optional<SettingsPortals> findByid(String id);
}
