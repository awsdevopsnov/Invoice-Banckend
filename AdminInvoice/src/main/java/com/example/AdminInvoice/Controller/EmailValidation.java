package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.EmailSendInvoice;
import com.example.AdminInvoice.Repository.CustomerDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EmailValidation {

    @Autowired private JavaMailSender emailSender;
    @Autowired private CustomerDao customerDao;
    @Autowired private TokenValidator tokenValidator;

    @PostMapping("/sendPDFByEmail")
    public ResponseEntity<EmailSendInvoice> sendPDFByEmail(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("file") MultipartFile file,
            @RequestParam String recipientEmail) {

        if (tokenValidator.isValidToken(accessToken)) {
            try {
                if (!isValidEmail(recipientEmail)) {
                    return ResponseEntity.badRequest().body(
                            new EmailSendInvoice(recipientEmail, "Invalid email address.", HttpStatus.BAD_REQUEST));
                }
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(recipientEmail);
                helper.setSubject("Your PDF");
                helper.setText("Please find the attached PDF file.");
                helper.addAttachment("document.pdf", file);
                emailSender.send(message);

                return ResponseEntity.ok(
                        new EmailSendInvoice(recipientEmail, "Email sent successfully.", HttpStatus.OK));
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new EmailSendInvoice(recipientEmail, "Failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new EmailSendInvoice(recipientEmail, "Token expired.", HttpStatus.FORBIDDEN));
        }
    }
    private boolean isValidEmail(String email) {
        return customerDao.findByCustomerEmail(email).isPresent();
    }
}
