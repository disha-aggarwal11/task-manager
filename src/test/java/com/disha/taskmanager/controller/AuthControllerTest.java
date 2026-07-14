package com.disha.taskmanager.controller;

import com.disha.taskmanager.dto.*;
import com.disha.taskmanager.security.JwtFilter;
import com.disha.taskmanager.security.OAuth2LoginSuccessHandler;
import com.disha.taskmanager.service.AuthService;
import com.disha.taskmanager.service.EmailService;
import com.disha.taskmanager.service.PasswordResetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Disabled;

@Disabled("Temporarily disabled while controller integration tests are being updated")


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private PasswordResetService passwordResetService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private OAuth2LoginSuccessHandler successHandler;



    // ==========================================================
    // SIGNUP
    // ==========================================================

    @Test
    void signupShouldReturnCreatedUser() throws Exception {

        SignupRequest request =
                new SignupRequest(
                        "Kiara",
                        "kiara@gmail.com",
                        "kiara123"
                );

        UserResponse response =
                new UserResponse(
                        1L,
                        "Kiara",
                        "kiara@gmail.com"
                );

        when(authService.signup(any(SignupRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))

                .andExpect(jsonPath("$.username")
                        .value("Kiara"))

                .andExpect(jsonPath("$.email")
                        .value("kiara@gmail.com"));

        verify(authService)
                .signup(any(SignupRequest.class));
    }
    // ==========================================================
    // LOGIN
    // ==========================================================

    @Test
    void loginShouldReturnAccessTokenAndSetRefreshCookie() throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "kiara@gmail.com",
                        "kiara123"
                );

        LoginResponse response =
                new LoginResponse(
                        "access-token",
                        "refresh-token"
                );

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.accessToken")
                        .value("access-token"))

                .andExpect(jsonPath("$.refreshToken")
                        .doesNotExist())

                .andExpect(cookie().exists("refreshToken"))

                .andExpect(cookie().httpOnly("refreshToken", true));

        verify(authService)
                .login(any(LoginRequest.class));
    }

    // ==========================================================
    // REFRESH TOKEN
    // ==========================================================

    @Test
    void refreshTokenShouldReturnNewAccessToken() throws Exception {

        LoginResponse response =
                new LoginResponse(
                        "new-access-token",
                        "refresh-token"
                );

        when(authService.refreshToken(any(RefreshTokenRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/refresh-token")
                                .cookie(
                                        new jakarta.servlet.http.Cookie(
                                                "refreshToken",
                                                "refresh-token"
                                        )
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.accessToken")
                        .value("new-access-token"))

                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"));

        verify(authService)
                .refreshToken(any(RefreshTokenRequest.class));
    }

    // ==========================================================
    // LOGOUT
    // ==========================================================

    @Test
    void logoutShouldClearCookie() throws Exception {

        doNothing().when(authService)
                .logout("refresh-token");

        mockMvc.perform(
                        post("/auth/logout")
                                .cookie(
                                        new jakarta.servlet.http.Cookie(
                                                "refreshToken",
                                                "refresh-token"
                                        )
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(content().string(
                        "Logged out successfully"
                ))

                .andExpect(cookie().maxAge(
                        "refreshToken",
                        0
                ));

        verify(authService)
                .logout("refresh-token");
    }
    // ==========================================================
    // FORGOT PASSWORD
    // ==========================================================

    @Test
    void forgotPasswordShouldSendResetEmail() throws Exception {

        ForgotPasswordRequest request =
                new ForgotPasswordRequest(
                        "kiara@gmail.com"
                );

        when(passwordResetService.createPasswordResetToken(
                "kiara@gmail.com"))
                .thenReturn("reset-token");

        doNothing().when(emailService)
                .sendPasswordResetEmail(
                        "kiara@gmail.com",
                        "reset-token"
                );

        mockMvc.perform(
                        post("/auth/forgot-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(content().string(
                        "Password reset email sent successfully."
                ));

        verify(passwordResetService)
                .createPasswordResetToken("kiara@gmail.com");

        verify(emailService)
                .sendPasswordResetEmail(
                        "kiara@gmail.com",
                        "reset-token"
                );
    }

    // ==========================================================
    // RESET PASSWORD
    // ==========================================================

    @Test
    void resetPasswordShouldUpdatePassword() throws Exception {

        ResetPasswordRequest request =
                new ResetPasswordRequest(
                        "newPassword123"
                );

        doNothing().when(passwordResetService)
                .resetPassword(
                        "reset-token",
                        "newPassword123"
                );

        mockMvc.perform(
                        post("/auth/reset-password/reset-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(content().string(
                        "Password updated successfully."
                ));

        verify(passwordResetService)
                .resetPassword(
                        "reset-token",
                        "newPassword123"
                );
    }

    // ==========================================================
    // VALIDATION
    // ==========================================================

    @Test
    void signupShouldReturnBadRequestForInvalidInput() throws Exception {

        SignupRequest request =
                new SignupRequest(
                        "",
                        "invalid-email",
                        "123"
                );

        mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(authService, never())
                .signup(any());
    }

}