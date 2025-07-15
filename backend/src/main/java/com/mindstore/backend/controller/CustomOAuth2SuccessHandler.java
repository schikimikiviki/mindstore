package com.mindstore.backend.controller;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.AuthenticationService;
import com.mindstore.backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AuthenticationService authService;

    public CustomOAuth2SuccessHandler(JwtService jwtService, AuthenticationService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    /**
     *
     * @param request - the request that was made
     * @param response - the extended response
     * @param authentication of the user
     * @throws IOException for sendRedirect
     * function: this controller is used to redirect the user after a oAuth login
     * it redirects back to /
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {


        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = authService.findOrCreateUserByEmail(email);

        String jwt = jwtService.generateToken(user);

        Cookie cookie = new Cookie("token", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);

        response.setHeader("Set-Cookie",
                "token=" + jwt + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=3600");


        // redirect to frontend
        response.sendRedirect("http://localhost:4200/");
    }
}
