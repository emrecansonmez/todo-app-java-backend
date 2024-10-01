package com.thy.todoapp.services;

import com.thy.todoapp.dtos.LoginUserDto;
import com.thy.todoapp.dtos.RegisterUserDto;
import com.thy.todoapp.entities.User;
import com.thy.todoapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_ShouldCreateUserSuccessfully() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@test.com");
        registerUserDto.setFullName("Test User");
        registerUserDto.setPassword("password");

        User user = new User();
        user.setEmail("test@test.com");
        user.setFullName("Test User");
        user.setPassword("encodedPassword");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authenticationService.signup(registerUserDto);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_ShouldAuthenticateUserSuccessfully() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@test.com");
        loginUserDto.setPassword("password");

        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        authenticationService.authenticate(loginUserDto);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("test@test.com", "password")
        );
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void authenticate_ShouldThrowExceptionWhenUserNotFound() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("notfound@test.com");
        loginUserDto.setPassword("password");

        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(loginUserDto));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("notfound@test.com", "password")
        );
        verify(userRepository).findByEmail("notfound@test.com");
    }
}
