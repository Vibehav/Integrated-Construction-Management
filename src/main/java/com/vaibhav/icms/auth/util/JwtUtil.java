package com.vaibhav.icms.auth.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
    
    // 1. CONFIG VALUES FROM APPLICATION PROPERTIES
    // =============================================

    // Base64 Encoded secret key 
    @Value("${jwt.secret}") 
    private String secretKey;


    //Expiration Time 24 hours
    @Value("${jwt.expiration}")
    private long expirationTime;


     // 2. SIGNING KEY
    // ================

    /*
      Converts the Base64 secret string into a SecretKey object
      that JJWT uses to sign and verify tokens.
      Basically this function converts the secret Key from Application properties to a cryptographic key.
     */

      private SecretKey getSigningKey(){   
        byte[] rawBytes = Decoders.BASE64.decode(secretKey); // converts the string into rawbytes
        return Keys.hmacShaKeyFor(rawBytes); // converts the rawBytes into a proper cryptographic key
      }
    //  it's like a password that proves the given token is legggiiiitttttt.


     // 3. TOKEN GENERATION
    // ==========================

    /*
    generating token for the user. Token includes email,issue date, expirationDate + expiration time, roles to authorize the user and cryptographic key
    */
    public String generateToken(UserDetails userDetails){
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + expirationTime);
        List<String> role = new ArrayList<>();   // use map for multiple claims ( roles, company id)
        for(var authority : userDetails.getAuthorities()){
            role.add(authority.getAuthority());
        }

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .claim("roles", role)
                .signWith(getSigningKey())
                .compact();          
    }

     // 4. TOKEN VALIDATION
     // Watchman of the building
    // ==========================


    // A. Check if token is still valid for a particular user
    // it checks if the username belongs to the same user
    // and the expiration time is within 24hrs
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token); 
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    // 5. CLAIM EXTRACTION HELPERS
    // A tool/function to extract required data which helps to verify the token
    // ==========================================


    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    public Date extractExpiration(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

     //  5. THE BRAIN
    //  =============================
     /* Core parse method:
     * Verifies signature using our SecretKey and returns Claims (payload).
     */
   
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())    // verify signature
                .build()
                .parseSignedClaims(token)      // token must be signed (JWS)
                .getPayload();                // get the body (claims)
    }

}
