package com.example.AdminInvoice.JwtToken;

import com.example.AdminInvoice.Controller.LoginMessage;
import com.example.AdminInvoice.Entity.Login;
import com.example.AdminInvoice.Repository.LoginDao;
import com.example.AdminInvoice.Repository.RegisterDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtill {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600000; // 1 hour in milliseconds
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L; // 30 days in milliseconds

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private RegisterDao registerDao;

    private static final Logger LOGGER = Logger.getLogger(JwtUtill.class.getName());

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid token", e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Token validation failed", e);
            return false;
        }
    }

    public LoginMessage refreshAccessTokenUsingRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            return new LoginMessage("Refresh token expired", false, "", "", "", "", HttpStatus.UNAUTHORIZED);
        }

        Claims claims = getClaimsFromToken(refreshToken);
        if (claims == null || !"refresh".equals(claims.get("type"))) {
            return new LoginMessage("Invalid refresh token", false, "", "", "","",HttpStatus.UNAUTHORIZED);
        }

        String username = claims.getSubject();
        String newAccessToken = generateAccessToken(username);

        Optional<Login> optionalLogin = loginDao.findByRefreshToken(refreshToken);
        if (optionalLogin.isPresent()) {
            Login loginToUpdate = optionalLogin.get();
            loginToUpdate.setAccessToken(newAccessToken);
            loginDao.save(loginToUpdate);
            return new LoginMessage("Access token updated successfully", true, newAccessToken, refreshToken, loginToUpdate.getUserRole(), loginToUpdate.getUsername(), HttpStatus.OK);
        } else {
            return new LoginMessage("Refresh token not found", false, "", refreshToken, "", "", HttpStatus.NOT_FOUND);
        }
    }
}