package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
public class HashService {

    public final String generateHash(final User user) {
        log.info("C=HashService, M=generateHash; T=User {}", user);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            final String toHash = user.toString()+ LocalDateTime.now().toString();
            messageDigest.update(toHash.getBytes("UTF-8"));
            final byte[] hashBytes = messageDigest.digest();
            StringBuffer hash = new StringBuffer();

            for (byte b : hashBytes)
                hash.append(String.format("%02x", b & 0xff));

            return hash.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final String encryptHash(final String token) {
        log.info("C=HashService, M=encryptHash; T=Token {}", token);

        try {
            return Base64.getUrlEncoder().encodeToString(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final String decryptHash(String token) {
        log.info("C=HashService, M=decryptHash; T=TokenBase64 {}", token);

        try {
            return new String(Base64.getUrlDecoder().decode(token.getBytes("UTF-8")));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
