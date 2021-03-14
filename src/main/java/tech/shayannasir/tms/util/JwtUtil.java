package tech.shayannasir.tms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.entity.InvalidJwt;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.enums.Role;
import tech.shayannasir.tms.repository.InvalidJwtRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.minutes}")
    private Long tokenValidity;

    @Value("${jwt.sih.user.expiration.days}")
    private Long userTokenValidityDays;

    @Autowired
    private InvalidJwtRepository invalidJwtRepository;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Claims getClaimsFromExpiredToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getClaimFromToken(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (ExpiredJwtException jxt) {
            return true;
        }
    }

    public String generateToken(Authentication authentication) {

        if (authentication.getPrincipal() != null
                && ((User) authentication.getPrincipal()).getRole().toString().equals(Role.USER.toString())) {
            return generateTokenForUser(authentication.getName());
        }
        return generateToken(authentication.getName());
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (tokenValidity * 60L * 1000L)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String generateTokenForUser(String username) {

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (userTokenValidityDays * 24L * 60L * 60L * 1000L)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean isTokenValid(String token) {
        return !invalidJwtRepository.existsByToken(token);
    }

    public void inValidateToken(String token) {
        InvalidJwt jwtToken = new InvalidJwt();
        jwtToken.setToken(token);
        invalidJwtRepository.save(jwtToken);
    }

}
