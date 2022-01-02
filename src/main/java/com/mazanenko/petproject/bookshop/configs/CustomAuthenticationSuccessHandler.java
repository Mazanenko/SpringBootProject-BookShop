package com.mazanenko.petproject.bookshop.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        loggingAuthentication(authentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void loggingAuthentication(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        Collection<? extends GrantedAuthority> list = authentication.getAuthorities();
        User user = (User) authentication.getPrincipal();

        if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            LOGGER.info("Admin successfully authenticated.");
        } else if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
            LOGGER.info("Manager with email {} successfully authenticated.", user.getUsername());
        } else if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"))) {
            LOGGER.info("Customer with email {} successfully authenticated.", user.getUsername());
        }
    }
}
