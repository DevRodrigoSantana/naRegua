package br.com.naregua.jwt;


import br.com.naregua.entity.AppUser;
import br.com.naregua.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class JWTUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JWTUtils jwtUtils;

    private static final long ACCESS_TOKEN_MINUTES = 1;
    private static final long REFRESH_TOKEN_MINUTES = 2;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userService.buscarPorEmail(email);
        return new JWTUserDetails(user);
    }

    public JWTTokenPair getTokenPair(String email) {
        AppUser user = userService.buscarPorEmail(email);
        UUID id = user.getId();
        String role = user.getRole().name();

        JWTToken accessToken =
                jwtUtils.createAccessToken(id, email, role, ACCESS_TOKEN_MINUTES);

        JWTToken refreshToken =
                jwtUtils.createRefreshToken(id, email, REFRESH_TOKEN_MINUTES);

        return new JWTTokenPair(accessToken, refreshToken);
    }

    public JWTToken getToken(String email) {
        AppUser user = userService.buscarPorEmail(email);
        UUID id = user.getId();
        String role = user.getRole().name();

        return jwtUtils.createAccessToken(id, email, role, ACCESS_TOKEN_MINUTES);
    }
}
