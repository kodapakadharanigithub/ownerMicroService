package com.hcl.ownermicroservice.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	 private String secret="miniproject";
	   
	   private Claims extractAllClaims(String token)
	   {
		   return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	   }
	   
	   public <T> T extractClaim(String token, Function<Claims,T> claimsResolver)
	   {
		   final Claims claims=extractAllClaims(token);
		   return claimsResolver.apply(claims);
	   }
	   public String extractUsername(String token)   
	   {
		  return extractClaim(token,Claims::getSubject); 
	   }
	   public Date extractExpiration(String token) 
	   {
		   return extractClaim(token,Claims::getExpiration); 
	   }
	   
	   private Boolean isTokenExpired(String token)
	   {
		   return extractExpiration(token).before(new Date());
	   }
	   
	   private String createToken(Map<String,Object> claims,String subject)
	   {
		   return Jwts.builder()
				   .setClaims(claims)
				   .setSubject(subject)
				   .setIssuedAt(new Date(System.currentTimeMillis()))   
				   .setExpiration(new Date(System.currentTimeMillis()+60*60*1000*10))
				   .signWith(SignatureAlgorithm.HS256, secret)
				   .compact();
	   }
	   public String generateToken(String username)
	   {
		   Map<String, Object> claims=new HashMap<>();
		   return createToken(claims,username);
	   }
	   public Boolean validateToken(String token, UserDetails userdetails)
	   {
		   final String username=extractUsername(token);
		   return (username.equals(userdetails.getUsername()) && !isTokenExpired(token));
	   }

}
