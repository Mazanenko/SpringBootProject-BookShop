package com.mazanenko.petproject.bookshop.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        loggingLogout(authentication);
        super.onLogoutSuccess(request, response, authentication);
    }

    private void loggingLogout(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        Collection<? extends GrantedAuthority> list = authentication.getAuthorities();
        User user = (User) authentication.getPrincipal();

        if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            LOGGER.info("Admin successfully logged out.");
        } else if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
            LOGGER.info("Manager with email {} successfully logged out.", user.getUsername());
        } else if (list.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"))) {
            LOGGER.info("Customer with email {} successfully logged out.", user.getUsername());
        }
    }
}
