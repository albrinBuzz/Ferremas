package com.ferremas.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ferremas.model.CustomUserDetails;
import com.ferremas.model.Usuario;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));


        if (userDetails instanceof CustomUserDetails customUserDetails) {
            Usuario usuario = customUserDetails.getUsuario();

            extraClaims.put("rutUsuario", usuario.getRutUsuario());
            extraClaims.put("correo", usuario.getCorreo());
            extraClaims.put("nombreusuario", usuario.getNombreusuario());
            extraClaims.put("firstLogin", usuario.isFirstLogin());
        }

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // correo
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Métodos personalizados para extraer información específica
    public String getCorreo(String token) {
        return getClaim(token, claims -> (String) claims.get("correo"));
    }

    public String getNombreUsuario(String token) {
        return getClaim(token, claims -> (String) claims.get("nombreusuario"));
    }

    public String getRutUsuario(String token) {
        return getClaim(token, claims -> (String) claims.get("rutUsuario"));
    }

    public boolean isFirstLogin(String token) {
        return getClaim(token, claims -> Boolean.TRUE.equals(claims.get("firstLogin")));
    }

    public List<String> getRoles(String token) {
        Object rolesObj = getClaim(token, claims -> claims.get("roles"));
        if (rolesObj instanceof Collection<?> col) {
            List<String> roles = new ArrayList<>();
            for (Object item : col) {
                if (item instanceof Map<?, ?> map && map.containsKey("authority")) {
                    roles.add(map.get("authority").toString());
                }
            }
            return roles;
        }
        return List.of();
    }
}
