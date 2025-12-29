package br.com.naregua.web.dto.mapper;


import br.com.naregua.entity.AppUser;
import br.com.naregua.web.dto.UserDTO;
import br.com.naregua.web.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static AppUser toUser(UserDTO dto){
        return  new ModelMapper().map(dto,AppUser.class);
    }
    public static UserResponseDTO toResponseDto(AppUser user){
        String role = user.getRole().name().substring("ROLE_".length());

        PropertyMap<AppUser, UserResponseDTO> props = new PropertyMap<AppUser, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return  mapper.map(user, UserResponseDTO.class);
    }
    public static List<UserResponseDTO> toListUsers(List<AppUser> users){
        return  users.stream().map(user -> toResponseDto(user)).collect(Collectors.toList());
    }

}