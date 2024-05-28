package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.User;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class HomeUser {
    @Autowired private UserService userService;
    @Autowired private TokenValidator tokenValidator;

    @PostMapping("/create")
    public ResponseEntity<AddedResponse<User>> createUser(@RequestHeader("Authorization") String accessToken, @Valid @RequestBody User user) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                AddedResponse<User> responseUser = userService.addUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(null, "Token expired.", HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AddedResponse<>(null, e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/get/{idOrEmail}")
    public ResponseEntity<?> getUserByIdOrEmail(@RequestHeader("Authorization") String accessToken, @PathVariable String idOrEmail) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                User user = userService.getUserByIdOrEmail(idOrEmail);
                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String accessToken) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                List<User> userList = userService.getallUser();
                return ResponseEntity.ok(userList);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String accessToken, @PathVariable String userId, @RequestBody User user) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                UpdateResponse updateResponse = userService.updateUser(userId, user);
                return ResponseEntity.ok(updateResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken, @PathVariable String userId) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                DeleteResponse deleteResponse = userService.deleteUser(userId);
                return ResponseEntity.ok(deleteResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}