package yoon.cloud.board.gatewayService.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.exp}")
    private long exp;

    public Key getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp*1000l*60))
                .signWith(getKey())
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJwt(token).getBody();
    }

    public Date getExpiration(String token){
        return getClaims(token).getExpiration();
    }

    public String getEmail(String token){
        return (String) getClaims(token).get("email");
    }

    public boolean validated(String token){
        return !getExpiration(token).before(new Date());
    }

}
