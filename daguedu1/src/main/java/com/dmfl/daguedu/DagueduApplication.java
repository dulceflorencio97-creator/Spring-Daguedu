package com.dmfl.daguedu;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.security.Keys;

@SpringBootApplication
public class DagueduApplication {

    public static void main(String[] args) {
        SpringApplication.run(DagueduApplication.class, args);

        SecretKey key=Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String secret= Base64.getEncoder().encodeToString(key.getEncoded());
    
    System.out.println("La clave secreta es: " + secret);



    }

}
