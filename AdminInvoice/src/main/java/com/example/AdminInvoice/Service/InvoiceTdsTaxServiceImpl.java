package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoiceTdsTax;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.InvoiceTdsTaxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceTdsTaxServiceImpl implements InvoiceTdsTaxService{
    @Autowired private InvoiceTdsTaxDao invoiceTdsTaxDao;
    @Override
    public AddedResponse<InvoiceTdsTax> addInvoiceTdsTax(InvoiceTdsTax invoiceTdsTax) throws Exception {
        Optional<InvoiceTdsTax> taxValidate=invoiceTdsTaxDao.findByTaxName(invoiceTdsTax.getTaxName());
        if(taxValidate.isPresent()){
            return new AddedResponse<>(invoiceTdsTax.getId(), "TDS Tax Is Already Exist!", HttpStatus.CONFLICT);
        }
        invoiceTdsTaxDao.save(invoiceTdsTax);
        return new AddedResponse<>(invoiceTdsTax.getId(),"New TDS Tax Is Created",HttpStatus.OK);
    }

    @Override
    public InvoiceTdsTax getInvoiceTDSTax(String taxId) throws Exception {
        Optional<InvoiceTdsTax> getTDSTax=invoiceTdsTaxDao.findById(taxId);
            return getTDSTax.get();
    }

    @Override
    public UpdateResponse updateInvoiceTDSTax(String taxId, InvoiceTdsTax invoiceTdsTax) throws Exception {
        try {
            Optional<InvoiceTdsTax> taxTDS = invoiceTdsTaxDao.findById(taxId);
            if (taxTDS.isPresent()) {
                InvoiceTdsTax existingInvoiceTDSTax = taxTDS.get();
                existingInvoiceTDSTax.setTaxName(invoiceTdsTax.getTaxName());
                existingInvoiceTDSTax.setTaxPercentage(invoiceTdsTax.getTaxPercentage());
                invoiceTdsTaxDao.save(existingInvoiceTDSTax);
                return new UpdateResponse(existingInvoiceTDSTax.getId(), "TdsTax updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(invoiceTdsTax.getId(), "TdsTax not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(taxId, "Failed to update TdsTax: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }    }

    @Override
    public DeleteResponse deleteInvoiceTDSTax(String taxId) throws Exception {
        Optional<InvoiceTdsTax> invoiceTdsTax=invoiceTdsTaxDao.findById(taxId);
        if(invoiceTdsTax.isPresent()){
            invoiceTdsTaxDao.deleteById(taxId);
            return new DeleteResponse(taxId,"TdsTax deleted",HttpStatus.OK);
        }
        return new DeleteResponse(taxId,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<InvoiceTdsTax> getAllInvoiceTDSTax() throws Exception {
        return invoiceTdsTaxDao.findAll();
    }
}
