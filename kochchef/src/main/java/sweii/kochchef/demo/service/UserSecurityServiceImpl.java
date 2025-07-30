package sweii.kochchef.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



@Service
public class UserSecurityServiceImpl implements UserSecurityService {


    protected AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityServiceImpl.class);

    @Override
    public String findLoggedInEmail() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if(userDetails instanceof UserDetails userDetails1) {
            return userDetails1.getUsername(); //By Email
        }

        return null;
    }

    @Override
    public void autoLogin(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authenticatedToken = authenticationManager.authenticate(authenticationToken);
        if (authenticatedToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.debug("Automatischer login {}!", email);
        }
    }
}
