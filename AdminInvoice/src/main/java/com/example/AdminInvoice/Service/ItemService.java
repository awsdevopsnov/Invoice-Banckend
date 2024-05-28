package com.example.AdminInvoice.Service;
import com.example.AdminInvoice.Entity.Services;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import java.util.List;
public interface ItemService {

    public AddedResponse<Services> addService(Services services)throws Exception;
    public Services getServicesByIdOrCode(String IdOrAccounting)throws Exception;
    public List<Services> getallServices()throws Exception;
    public UpdateResponse updateService(String serviceId, Services services)throws Exception;
    public DeleteResponse delteService(String serviceId)throws Exception;

}
