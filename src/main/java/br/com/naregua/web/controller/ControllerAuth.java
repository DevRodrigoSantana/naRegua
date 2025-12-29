package br.com.naregua.web.controller;

import br.com.naregua.jwt.JWTToken;
import br.com.naregua.jwt.JWTTokenPair;
import br.com.naregua.jwt.JWTUserDetailsService;
import br.com.naregua.jwt.JWTUtils;
import br.com.naregua.web.dto.AuthResponse;
import br.com.naregua.web.dto.RefreshDTO;
import br.com.naregua.web.dto.UserLoginDTO;
import br.com.naregua.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ControllerAuth {

    private final JWTUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UserLoginDTO dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getEmail());

        try {
            // Autenticação
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);

            // Obter token pair
            JWTTokenPair tokens = detailsService.getTokenPair(dto.getEmail());

            AuthResponse response = new AuthResponse(
                    tokens.getAccessToken().getToken(),
                    tokens.getRefreshToken().getToken(),
                    "Bearer",
                    1 // tempo em minutos do access token
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            log.warn("Credenciais inválidas para o email: {}", dto.getEmail());
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas"));
        }
    }
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshDTO dto, HttpServletRequest request) {
        try {
            if (!jwtUtils.isTokenValid(dto.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorMessage(
                                request,
                                HttpStatus.UNAUTHORIZED,
                                "Refresh token inválido ou expirado"
                        ));
            }

            String email = jwtUtils.getEmailFromToken(dto.getRefreshToken());

            JWTToken newAccessToken = detailsService.getToken(email);

            AuthResponse response = new AuthResponse(
                    newAccessToken.getToken(),
                    dto.getRefreshToken(),
                    "Bearer",
                    newAccessToken.getTimeToken()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erro ao atualizar token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            request,
                            HttpStatus.BAD_REQUEST,
                            "Erro ao atualizar token"
                    ));
        }
    }
}