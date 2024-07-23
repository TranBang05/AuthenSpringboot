
package com.example.demo_mhdigital.config.authconfig;


import com.example.demo_mhdigital.data.user.UserAuthorDTO;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import org.com.app.tables.pojos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;



@Component
@Service
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @Value("abcdefghijklmnOPQRSTUVWXYZ")
    private String SECRET_KEY;

    public String generateAccessToken(User user, List<String> roles) {
        UserAuthorDTO dto = new UserAuthorDTO()
                .setUserId(user.getId())
                .setRoles(roles);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(json)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}

