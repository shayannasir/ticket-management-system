package tech.shayannasir.tms.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.service.UserService;
import tech.shayannasir.tms.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = httpServletRequest.getHeader(Constants.TOKEN_HEADER);
        String jwt = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(Constants.TOKEN_PREFIX)) {
            jwt = requestTokenHeader.substring(Constants.ESCAPE_BEARER);

            try {
                if (StringUtils.countMatches(jwt, '.') == 2 && jwtUtil.isTokenValid(jwt)) {

                    String username = jwtUtil.getUsernameFromToken(jwt);
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (username.equals(userDetails.getUsername()) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
                        // if token is valid configure Spring Security to manually set authentication
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired " + jwt);
            } catch (Exception e) {
                log.error("Error While validating token ", e);
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
