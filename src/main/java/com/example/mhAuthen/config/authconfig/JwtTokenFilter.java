package com.example.demo_mhdigital.config.authconfig;

import com.example.demo_mhdigital.data.user.UserAuthorDTO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtil;


    @Value("abcdefghijklmnOPQRSTUVWXYZ")
    private String SECRET_KEY;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);

        filterChain.doFilter(request, response);
    }


    // kiểm tra xem header Authorization có chứa thông tin của jwt khong
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }


    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            String token = header.split(" ")[1].trim();
            return token;
        }
        return "err";
    }



    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserAuthorDTO userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, getAuthorities(userDetails));

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));// luu thong tin user trên web

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserAuthorDTO getUserDetails(String token) {
        String subject = jwtUtil.getSubject(token);
        if (subject == null) return null;

        Gson gson = new Gson();
        UserAuthorDTO userAuthorDTO = gson.fromJson(subject, UserAuthorDTO.class);

        return userAuthorDTO;
    }

   /* private UserAuthorDTO getUserFromJWT(String token){
        Claims claims= Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
        return new JsonObject(claims)
                .mapTo(UserAuthorDTO.class);
    }*/


    // lay ra 1 list vai tro

    private List<SimpleGrantedAuthority> getAuthorities(UserAuthorDTO userInfo) {
        return Optional.ofNullable(userInfo.getRoles())
                .orElse(Collections.emptyList())
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(toList());
    }

}
