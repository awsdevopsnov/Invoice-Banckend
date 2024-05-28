package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoicePaymentTerms;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.InvoicePaymentTermsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
@Service
public class InvoicePaymentTermsServiceImpl implements InvoicePaymentTermsService{
    @Autowired
    private InvoicePaymentTermsDao invoicePaymentTermsDao;
    @Override
    public AddedResponse<InvoicePaymentTerms> invoiceAddedPaymentTerms(InvoicePaymentTerms invoicePaymentTerms) throws Exception {
        Optional<InvoicePaymentTerms> optionalInvoicePaymentTerms=invoicePaymentTermsDao.findByTermName(invoicePaymentTerms.getTermName());
        if(optionalInvoicePaymentTerms.isPresent()){
            return new AddedResponse<>(invoicePaymentTerms.getId(),"PaymentTerms already exists!", HttpStatus.CONFLICT);
        }
        invoicePaymentTermsDao.save(invoicePaymentTerms);
        return new AddedResponse<>(invoicePaymentTerms.getId(), "PaymentTerms added successfully", HttpStatus.CREATED);
    }

    @Override
    public InvoicePaymentTerms getInvoicePaymentTerms(String termsId) throws Exception {
        Optional<InvoicePaymentTerms> getPaymentTerms=invoicePaymentTermsDao.findById(termsId);
        if(getPaymentTerms.isEmpty()) {
            throw new FileNotFoundException("Invoice payment terms not found");
        }
        return getPaymentTerms.get();
    }

    @Override
    public UpdateResponse updateInvoicePaymentTerms(String termsId, InvoicePaymentTerms invoicePaymentTerms) throws Exception {
        try {
            Optional<InvoicePaymentTerms> paymentTerms = invoicePaymentTermsDao.findById(termsId);
            if (paymentTerms.isPresent()) {
                InvoicePaymentTerms existingInvoicePaymentTerms = paymentTerms.get();
                existingInvoicePaymentTerms.setTermName(invoicePaymentTerms.getTermName());
                existingInvoicePaymentTerms.setTotalDays(invoicePaymentTerms.getTotalDays());
                invoicePaymentTermsDao.save(existingInvoicePaymentTerms);
                return new UpdateResponse(existingInvoicePaymentTerms.getId(), "PaymentTerms updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(invoicePaymentTerms.getId(), "PaymentTerms not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(termsId, "Failed to update PaymentTerms: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteResponse deleteInvoicePaymentTerms(String termsId) throws Exception {
        Optional<InvoicePaymentTerms> invoicePaymentTerms=invoicePaymentTermsDao.findById(termsId);
        if(invoicePaymentTerms.isPresent()){
            invoicePaymentTermsDao.deleteById(termsId);
            return new DeleteResponse(termsId,"PaymentTerms deleted",HttpStatus.OK);
        }
        return new DeleteResponse(termsId,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<InvoicePaymentTerms> getAllInvoicePaymentTerms() throws Exception {
        return invoicePaymentTermsDao.findAll();
    }
}
