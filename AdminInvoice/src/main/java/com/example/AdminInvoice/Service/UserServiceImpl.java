package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.User;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserDao userDao;

    @Override
    public AddedResponse<User> addUser(User user)throws Exception {
            Optional<User> existingUser = userDao.findByUserEmail(user.getUserEmail());
            if (existingUser.isPresent()) {
                return new AddedResponse<>(user.getId(), "User Already Exists", HttpStatus.CONFLICT);
            }
            User savedUser = userDao.save(user);
            return new AddedResponse<>(savedUser.getId(), "User added successfully", HttpStatus.CREATED);
    }

    @Override
    public User getUserByIdOrEmail(String idOrEmail) throws Exception {
        Optional<User> userById = userDao.findByUserEmail(idOrEmail);
        Optional<User> userByEmail = userDao.findByUserEmail(idOrEmail);
        if (userById.isPresent()) {
            return userById.get(); // User found by ID
        } else if (userByEmail.isPresent()) {
            return userByEmail.get(); // User found by email
        }
        return null; // User not found
    }
    @Override
    public List<User> getallUser() throws Exception {
        return userDao.findAll();
    }

    @Override
    public UpdateResponse updateUser(String userId, User user) throws Exception {
        Optional<User> userOptional=userDao.findByUserEmail(userId);
        if(userOptional.isPresent()) {
            User user1 = userOptional.get();
            user1.setUserEmail(user.getUserEmail());
            user1.setUserName(user.getUserName());
            user1.setUserDesignation(user.getUserDesignation());
            user1.setUserRole(user.getUserRole());
            user1.setUserAccess(user.getUserAccess());
            user1.setPhoneNumber(user.getPhoneNumber());
            userDao.save(user1);
            return new UpdateResponse(userId,"Updated User", HttpStatus.OK);
        }
        else {
            return new UpdateResponse(userId,"Invalid User",HttpStatus.UNAUTHORIZED);

        }
    }

    @Override
    public DeleteResponse deleteUser(String userId) throws Exception {
        Optional<User> userOptional=userDao.findById(userId);
        if(userOptional.isPresent()) {
            userDao.deleteById(userId);
            return new DeleteResponse(userId,"Deleted User",HttpStatus.OK);
        }
        else {
            return new DeleteResponse(userId,"Invalid User",HttpStatus.UNAUTHORIZED);

        }
    }
}

