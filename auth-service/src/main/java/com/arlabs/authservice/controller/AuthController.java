package com.arlabs.authservice.controller;

import com.arlabs.authservice.dto.LoginRequestDTO;
import com.arlabs.authservice.dto.LoginResponseDTO;
import com.arlabs.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if(tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validatToken(@RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return  authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
