package com.example.AdminInvoice.Service;

import ch.qos.logback.core.net.server.Client;
import com.example.AdminInvoice.Entity.Customer;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface CustomerService {

    public AddedResponse<Customer> addClient(Customer client)throws Exception;

    public Customer findbyId(String id)throws Exception;

    public List<Customer> getallClient()throws Exception;

    public UpdateResponse updateClient(String id, Customer client)throws Exception;

    public DeleteResponse deleteClient(String id)throws Exception;
}
