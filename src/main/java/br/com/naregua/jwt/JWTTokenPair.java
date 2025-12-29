package br.com.naregua.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenPair {
    private JWTToken accessToken;
    private JWTToken refreshToken;
}
