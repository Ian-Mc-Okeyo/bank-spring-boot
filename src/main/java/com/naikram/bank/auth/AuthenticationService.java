package com.naikram.bank.auth;

import com.naikram.bank.config.JwtService;
import com.naikram.bank.exceptions.EmailAlreadyExistsException;
import com.naikram.bank.exceptions.WrongLoginCredentialsException;
import com.naikram.bank.user.User;
import com.naikram.bank.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request)
            throws EmailAlreadyExistsException
    {
        //check if a user with that email exists
        if(repository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email Already Exists");
        }

        //check that confirm password and password are similar

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            System.out.println("USer authenticated");

            var user = repository.findByEmail(request.getEmail()).orElseThrow(()
                    -> new WrongLoginCredentialsException("Wrong details")
            );
            var jwtToken = jwtService.generateToken((User) user);

            return AuthenticationResponse.builder()
                    .firstName(((User) user).getFirstName())
                    .lastName(((User) user).getLastName())
                    .accessToken(jwtToken)
                    .refreshToken(jwtToken)
                    .build();
        }catch (AuthenticationException e){
            throw new WrongLoginCredentialsException("Wrong details");
        }
    }
}
