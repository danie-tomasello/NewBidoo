package com.innovat.userservice.utility;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.innovat.userservice.dto.CheckUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "iat";
    static final String CLAIM_KEY_AUTHORITIES = "roles";
    static final String CLAIM_KEY_IS_ENABLED = "isEnabled";


    @Value("${jwt.secret}")
    static private String secret;



    static public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    static public CheckUser getUserDetails(String token) {

        if(token == null){
            return null;
        }
        try {
            final Claims claims = getClaimsFromToken(token);
            List<String> authorities = null;
            if (claims.get(CLAIM_KEY_AUTHORITIES) != null) {
                authorities = (List<String>) claims.get(CLAIM_KEY_AUTHORITIES);
            }

            return new CheckUser(
                    claims.getSubject(),
                    "",
                    authorities,
                    (boolean) claims.get(CLAIM_KEY_IS_ENABLED)
            );
        } catch (Exception e) {
            return null;
        }

    }


    static private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


}