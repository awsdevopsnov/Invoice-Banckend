package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Controller.LoginMessage;
import com.example.AdminInvoice.Entity.ChangePassword;
import com.example.AdminInvoice.Entity.Login;
import com.example.AdminInvoice.Entity.Register;
import com.example.AdminInvoice.Payroll.ChangeNewPasswordResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.ForgetResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface RegisterService {

    public Register addRegister(Register register)throws Exception;

    public Register findByUsername(String userName)throws Exception;

    public UpdateResponse updateRegister(String id, Register register)throws Exception;

    public DeleteResponse deleteRegister(String id)throws Exception;

    public List<Register> registerList()throws Exception;

    public ForgetResponse findbyEmail(String userEmail)throws Exception;

    public UpdateResponse updatePassword(String userEmail, Register register)throws Exception;

    public LoginMessage getUserByValidate(Login login)throws Exception;

    public LoginMessage refreshTokenPerformRegeneratedAccessToken(String refresh)throws Exception;

    public ChangeNewPasswordResponse changePassword(String userName, ChangePassword updatedRegister) throws Exception ;

}
