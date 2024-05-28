package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.User;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface UserService {

    public AddedResponse<User> addUser(User user) throws Exception;

    public User getUserByIdOrEmail(String idOrEmail) throws Exception;

    public List<User> getallUser() throws Exception;

    public UpdateResponse updateUser(String userId, User user) throws Exception;

    public DeleteResponse deleteUser(String userId) throws Exception;



}