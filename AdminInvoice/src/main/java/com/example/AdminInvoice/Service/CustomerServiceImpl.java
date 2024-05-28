package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.Customer;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired private CustomerDao customerDao;

    @Override
    public AddedResponse<Customer> addClient(Customer client) throws Exception{
            Optional<Customer> existingClient = customerDao.findByCustomerEmail(client.getCustomerEmail());
            if (existingClient.isPresent()) {
                return new AddedResponse<>(client.getId(),"Customer already exists!",HttpStatus.CONFLICT);
            }
            Customer savedClient = customerDao.save(client);
            return new AddedResponse<>(savedClient.getId(), "Customer added successfully", HttpStatus.CREATED);
    }

    @Override
    public Customer findbyId(String id) throws Exception {
        Optional<Customer> client = customerDao.findById(id);
        return client.get();
    }

    @Override
    public List<Customer> getallClient() throws Exception {
        return customerDao.findAll();
    }

    public UpdateResponse updateClient(String id, Customer customer) {
        try {
            Optional<Customer> optional = customerDao.findById(id);
            if (optional.isPresent()) {
                Customer existingCustomer = optional.get();
                existingCustomer.setCustomerType(customer.getCustomerType());
                existingCustomer.setCustomerName(customer.getCustomerName());
                existingCustomer.setCompanyName(customer.getCompanyName());
                existingCustomer.setCustomerEmail(customer.getCustomerEmail());
                existingCustomer.setCustomerPhone(customer.getCustomerPhone());
                existingCustomer.setPaymentTerms(customer.getPaymentTerms());
                existingCustomer.setCountry(customer.getCountry());
                existingCustomer.setAddress(customer.getAddress());
                existingCustomer.setCity(customer.getCity());
                existingCustomer.setState(customer.getState());
                existingCustomer.setPinCode(customer.getPinCode());
                existingCustomer.setContactPersons(customer.getContactPersons());
                customerDao.save(existingCustomer);
                return new UpdateResponse(existingCustomer.getId(), "Customer updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(id, "Customer not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(id, "Failed to update customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteResponse deleteClient(String id)throws Exception {
        Optional<Customer> optional= customerDao.findById(id);
        if(optional.isPresent()){
            customerDao.deleteById(id);
            return new DeleteResponse(id,"Customer deleted",HttpStatus.OK);
        }
        return new DeleteResponse(id,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }

}

