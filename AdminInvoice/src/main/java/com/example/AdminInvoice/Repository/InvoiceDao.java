package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.InvoiceDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceDao extends MongoRepository<InvoiceDto,String> {
    List<InvoiceDto> findByInvoiceDateBetween(Date startDate,Date endDate);
}
