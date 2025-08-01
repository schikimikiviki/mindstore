package com.mindstore.backend.controller;

import com.mindstore.backend.data.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindstore.backend.data.dto.LoginUserDto;
import com.mindstore.backend.data.dto.RegisterUserDto;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.AuthenticationService;
import com.mindstore.backend.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     *
     * @param registerUserDto the Dto with user data
     * function: used to register users
     * @return ok --> if successfull
     */
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {

        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    /**
     *
     * @param request the path that the user tries to access
     * function: check if a user is currently logged in
     * for example when setting the login state in the frontend
     * returns the token, and the time left for the token validity
     * @return ok --> if successfull, else return 401 Unauthorized
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Extract JWT from cookie
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token cookie");
        }

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            try {
                long remainingMillis = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();
                long remainingSeconds = Math.max(remainingMillis / 1000, 0);

                return ResponseEntity.ok().body(
                        new LoginResponse()
                                .setToken(token)
                                .setExpiresIn(remainingSeconds)
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     *
     * @param loginUserDto with the email and password
     * @param response the extended response
     * function: login a user and set a cookie
     * @return ok ---> if successfull
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        // Create HTTP-only cookie
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);     // Ensures cookie is sent only over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        //cookie.setDomain("your-domain.com");
        response.addCookie(cookie);


        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    /**
     *
     * @param response - the extended response
     * function: log out a user, used in frontend to delete the set cookie
     * @return ok ---> if successfull
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Remove the JWT cookie
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Immediately expire the cookie

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }





}