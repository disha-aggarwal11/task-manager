package com.disha.taskmanager.security;

import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.UserRepository;
import com.disha.taskmanager.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public OAuth2LoginSuccessHandler(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService, RefreshTokenService refreshTokenService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;

    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        UserEntity user = repository.findByEmail(email)
                .orElseGet(() -> {

                    UserEntity newUser = new UserEntity();

                    newUser.setEmail(email);

                    newUser.setUsername(name);

                    newUser.setPassword(
                            passwordEncoder.encode(
                                    UUID.randomUUID().toString()
                            )
                    );

                    newUser.setRole(Role.USER);

                    return repository.save(newUser);
                });

        String accessToken =
                jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        Cookie cookie = new Cookie(
                "refreshToken",
                refreshToken.getToken()
        );
        cookie.setHttpOnly(true);

        cookie.setSecure(false);

        cookie.setPath("/");

        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);

        response.sendRedirect(
                "http://localhost:5173/oauth-success?token=" + accessToken
        );
    }
}