package br.com.naregua.web.dto;
import java.util.UUID;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {

    private UUID id;
    private String username;
    private String email;
    private String role;
}

