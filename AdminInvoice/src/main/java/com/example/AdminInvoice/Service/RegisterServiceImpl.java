package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Controller.LoginMessage;
import com.example.AdminInvoice.Entity.*;
import com.example.AdminInvoice.JwtToken.JwtUtill;
import com.example.AdminInvoice.Payroll.ChangeNewPasswordResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.ForgetResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.LoginDao;
import com.example.AdminInvoice.Repository.RegisterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired private RegisterDao registerDao;
    @Autowired private LoginDao loginDao;
    @Autowired private JwtUtill jwtUtill;

    @Override
    public Register addRegister(Register register) throws Exception {
        Optional<Register> registerOptional = registerDao.findByUsername(register.getUsername());
        if (registerOptional.isPresent()) {
            throw new UserAlreadyExistsException(register.getUsername() + " : User already exists!");
        }

        Role role = register.getUserRole();
        if (role == null) {
            throw new IllegalArgumentException("Role is required!");
        }

        switch (role) {
            case SUPERADMIN:
            case ADMIN:
            case APPROVER:
            case ENDUSER:
                break;
            default:
                throw new IllegalArgumentException("Invalid Role: " + role);
        }

        return registerDao.save(register);
    }

    @Override
    public Register findByUsername(String userName) throws Exception {
        return registerDao.findByUsername(userName).orElseThrow(() -> new UserNotFoundException("User Not Found with Name: "+ userName));
    }

    @Override
    public UpdateResponse updateRegister(String id, Register updatedRegister) throws Exception {
        Optional<Register> optional = registerDao.findById(id);
        if (optional.isPresent()) {
            Register existingRegister = optional.get();
            existingRegister.setUsername(updatedRegister.getUsername());
            existingRegister.setPassword(updatedRegister.getPassword());
            existingRegister.setUserRole(updatedRegister.getUserRole());
            existingRegister.setUserEmail(updatedRegister.getUserEmail());
            existingRegister.setUserAccess(updatedRegister.getUserAccess());
            existingRegister.setDescription(updatedRegister.getDescription());
            registerDao.save(existingRegister);
            return new UpdateResponse(id, "User details updated", HttpStatus.OK);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }


    @Override
    public DeleteResponse deleteRegister(String id) throws Exception {
        Optional<Register> optional = registerDao.findById(id);
        if (optional.isPresent()) {
            registerDao.deleteById(id);
            return new DeleteResponse(id, "User deleted successfully", HttpStatus.OK);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public List<Register> registerList() throws Exception {
        return registerDao.findAll();
    }

    @Override
    public ForgetResponse findbyEmail(String userEmail) throws Exception{
        Optional<Register> findEmail = registerDao.findByUserEmail(userEmail);
        if (findEmail.isPresent()) {
            return new ForgetResponse(userEmail, "Email is valid!", HttpStatus.OK);
        } else {
            return new ForgetResponse(userEmail, "Invalid email!", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UpdateResponse updatePassword(String userEmail, Register updatedRegister) throws Exception {
        Optional<Register> optional = registerDao.findByUserEmail(userEmail);
        if (optional.isPresent()) {
            Register existingRegister = optional.get();
            existingRegister.setPassword(updatedRegister.getPassword());
            registerDao.save(existingRegister);
            return new UpdateResponse(userEmail,"Password Updated",HttpStatus.OK);
        } else {
            return new UpdateResponse(userEmail,"Pls Recheck Password",HttpStatus.UNAUTHORIZED);
        }
    }


    public LoginMessage getUserByValidate(Login login) throws Exception {
        Optional<Register> optionalRegister = registerDao.findByUsername(login.getUsername());
        if (optionalRegister.isPresent()) {
            Register user = optionalRegister.get();
            String role = String.valueOf(user.getUserRole());
            if (user.getPassword().equals(login.getPassword())) {
                String accessToken = jwtUtill.generateAccessToken(user.getUsername());

                // Find the existing login entry
                Optional<Login> optionalLogin = loginDao.findByUsername(login.getUsername());
                String refreshToken;

                if (optionalLogin.isPresent()) {
                    Login existingLogin = optionalLogin.get();
                    refreshToken = existingLogin.getRefreshToken();

                    // Check if the current refresh token is still valid
                    if (!jwtUtill.validateToken(refreshToken)) {
                        refreshToken = jwtUtill.generateRefreshToken(user.getUsername());
                    }
                } else {
                    refreshToken = jwtUtill.generateRefreshToken(user.getUsername());
                }

                // Save or update the login details
                Login loginToUpdate = optionalLogin.orElse(new Login());
                loginToUpdate.setAccessToken(accessToken);
                loginToUpdate.setRefreshToken(refreshToken);
                loginToUpdate.setPassword(login.getPassword());
                loginToUpdate.setUsername(login.getUsername());
                loginToUpdate.setUserRole(role);
                loginDao.save(loginToUpdate);

                return new LoginMessage("Login successful", true, accessToken, refreshToken, role, login.getUsername(), HttpStatus.OK);
            } else {
                return new LoginMessage("Invalid password", false, "", "", "", login.getUsername(), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new LoginMessage("Username not found", false, "", "", "", login.getUsername(), HttpStatus.NOT_FOUND);
        }
    }

    public LoginMessage refreshTokenPerformRegeneratedAccessToken(String refreshToken) {
        return jwtUtill.refreshAccessTokenUsingRefreshToken(refreshToken);
    }

    // Change Password
    @Override
    public ChangeNewPasswordResponse changePassword(String userName, ChangePassword updatedRegister) {
        Optional<Register> optional = registerDao.findByUsername(userName);
        if (optional.isPresent()) {
            Register existingRegister = optional.get();
            if (existingRegister.getPassword().equals(updatedRegister.getCurrentPassword())) {
                existingRegister.setPassword(updatedRegister.getNewPassword());
                registerDao.save(existingRegister);
                return new ChangeNewPasswordResponse<>("Password updated successfully", HttpStatus.OK);
            } else {
                return new ChangeNewPasswordResponse<>("Incorrect current password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ChangeNewPasswordResponse<>("User ID not found", HttpStatus.NOT_FOUND);
        }
    }

    // Custom Exceptions
    public class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
