package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.Services;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired private ServiceDao serviceDao;

    @Override
    public AddedResponse<Services> addService(Services services) throws Exception{
        try {
            Optional<Services> optional = serviceDao.findByserviceAccountingCode(services.getServiceAccountingCode());
            if (optional.isPresent()) {
                return new AddedResponse<>(services.getId(), "Service Already Exists!",HttpStatus.CONFLICT);
            }
            Services savedService = serviceDao.save(services);
            return new AddedResponse<>(savedService.getId(), "Service added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new AddedResponse<>(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Services getServicesByIdOrCode(String IdOrAccounting) throws Exception {
        Optional<Services> serviceId=serviceDao.findById(IdOrAccounting);
        Optional<Services> serviceCode=serviceDao.findByserviceAccountingCode(IdOrAccounting);
        if(serviceId.isPresent()){
            return serviceId.get();
        } else if (serviceCode.isPresent()) {
            return serviceCode.get();
        }
        return null;
    }

    @Override
    public List<Services> getallServices() throws Exception {
        return serviceDao.findAll();
    }

    @Override
    public UpdateResponse updateService(String id, Services services) throws Exception {
        Optional<Services> finbyId = serviceDao.findById(id);
        if(finbyId.isPresent()) {
            Services idgetServices = finbyId.get();
            idgetServices.setServiceAccountingCode(services.getServiceAccountingCode());
            idgetServices.setServiceDescription(services.getServiceDescription());
            idgetServices.setServiceAmount(services.getServiceAmount());
            serviceDao.save(idgetServices);
            return new UpdateResponse(id,"Updated Service", HttpStatus.OK);
        }
             else {
                 return new UpdateResponse(id,"Invalid ServiceId",HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public DeleteResponse delteService(String id) throws Exception {
        Optional<Services> optional=serviceDao.findById(id);
        if(optional.isPresent()) {
            serviceDao.deleteById(id);
            return new DeleteResponse(id,"Deleted Service",HttpStatus.OK);
        }
        else {
            return new DeleteResponse(id,"Invalid Service Id",HttpStatus.UNAUTHORIZED);
        }
    }

}
