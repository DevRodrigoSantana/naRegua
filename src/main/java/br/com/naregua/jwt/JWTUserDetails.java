package br.com.naregua.jwt;


import br.com.naregua.entity.AppUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;


public class JWTUserDetails extends User {


   private AppUser usuario;


    public JWTUserDetails(AppUser user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));

    this.usuario = user;
    }

    public UUID getId(){
        return this.usuario.getId();
    }
    public String getRole(){
        return  this.usuario.getRole().name();
    }
    public String  getName(){return this.usuario.getUsername();}

}
