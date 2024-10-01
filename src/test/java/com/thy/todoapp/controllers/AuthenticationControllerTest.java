package com.thy.todoapp.controllers;

import com.thy.todoapp.dtos.LoginUserDto;
import com.thy.todoapp.dtos.RegisterUserDto;
import com.thy.todoapp.entities.User;
import com.thy.todoapp.services.AuthenticationService;
import com.thy.todoapp.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setControllerAdvice(new com.thy.todoapp.exceptions.GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_ShouldReturnRegisteredUser() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@test.com");
        registerUserDto.setFullName("Test User");
        registerUserDto.setPassword("password");

        User user = new User();
        user.setEmail("test@test.com");
        user.setFullName("Test User");

        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\", \"fullName\":\"Test User\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));

        verify(authenticationService).signup(any(RegisterUserDto.class));
    }

    @Test
    void authenticate_ShouldReturnJwtToken() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@test.com");
        loginUserDto.setPassword("password");

        User user = new User();
        user.setEmail("test@test.com");

        String token = "jwt-token";
        long expiresIn = 3600L;

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expiresIn);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.expiresIn").value(expiresIn));

        verify(authenticationService).authenticate(any(LoginUserDto.class));
        verify(jwtService).generateToken(user);
        verify(jwtService).getExpirationTime();
    }

    @Test
    void authenticate_ShouldReturnUnauthorized_WhenAuthenticationFails() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("wrong@test.com");
        loginUserDto.setPassword("wrongpassword");

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@test.com\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Invalid credentials"));

        verify(authenticationService).authenticate(any(LoginUserDto.class));
    }
}
