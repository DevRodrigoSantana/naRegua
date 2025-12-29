package br.com.naregua.repository;

import br.com.naregua.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);

    @Query("select u.role from AppUser u where u.email = :email")
    AppUser.Role findRoleByEmail(String email);
}
