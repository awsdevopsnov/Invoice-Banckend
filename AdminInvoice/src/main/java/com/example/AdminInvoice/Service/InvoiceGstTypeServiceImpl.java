package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoiceGstType;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.InvoiceGstTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceGstTypeServiceImpl implements InvoiceGstTypeService{
    @Autowired private InvoiceGstTypeDao invoiceGstTypeDao;

    @Override
    public AddedResponse<InvoiceGstType> addInvoiceGstType(InvoiceGstType invoiceGstType) throws Exception {
            Optional<InvoiceGstType> optionalInvoiceGstType=invoiceGstTypeDao.findByGstName(invoiceGstType.getGstName());
            if(optionalInvoiceGstType.isPresent()){
                return new AddedResponse<>(invoiceGstType.getGstName(),"GstType already exists!", HttpStatus.CONFLICT);
            }
            invoiceGstTypeDao.save(invoiceGstType);
            return new AddedResponse<>(invoiceGstType.getId(), "GstType added successfully", HttpStatus.CREATED);
    }

    @Override
    public InvoiceGstType getInvoiceGstType(String gstId) throws Exception {
        Optional<InvoiceGstType> getGstTx=invoiceGstTypeDao.findById(gstId);
        return getGstTx.get();
    }

    @Override
    public UpdateResponse updateInvoiceGstType(String gstId, InvoiceGstType invoiceGstType) throws Exception {
        try {
            Optional<InvoiceGstType> invoiceGst = invoiceGstTypeDao.findById(gstId);
            if (invoiceGst.isPresent()) {
                InvoiceGstType existingInvoiceGstType = invoiceGst.get();
                existingInvoiceGstType.setGstName(invoiceGstType.getGstName());
                existingInvoiceGstType.setGstPercentage(invoiceGstType.getGstPercentage());
                invoiceGstTypeDao.save(existingInvoiceGstType);
                return new UpdateResponse(existingInvoiceGstType.getId(), "GstType updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(invoiceGstType.getId(), "GstType not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(gstId, "Failed to update GstType: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteResponse deleteInvoiceGstType(String gstId) throws Exception {
        Optional<InvoiceGstType> invoiceGstType=invoiceGstTypeDao.findById(gstId);
        if(invoiceGstType.isPresent()){
            invoiceGstTypeDao.deleteById(gstId);
            return new DeleteResponse(gstId,"GstType deleted",HttpStatus.OK);
        }
        return new DeleteResponse(gstId,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<InvoiceGstType> getAllInvoiceGstType() throws Exception {
        return invoiceGstTypeDao.findAll();
    }

}
