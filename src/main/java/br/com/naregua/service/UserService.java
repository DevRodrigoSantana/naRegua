package br.com.naregua.service;

import br.com.naregua.entity.AppUser;
import br.com.naregua.exception.EmailUniqueViolationException;
import br.com.naregua.exception.EntityNotFoundException;
import br.com.naregua.exception.PasswordInvalidException;
import br.com.naregua.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* ============================
       CRIAR USUÁRIO
       ============================ */
    @Transactional
    public AppUser create(AppUser usuario) {

        if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailUniqueViolationException(
                    String.format("O e-mail '%s' já está cadastrado.", usuario.getEmail())
            );
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return userRepository.save(usuario);
    }

    /* ============================
       BUSCAR POR ID
       ============================ */
    @Transactional(readOnly = true)
    public AppUser findUserId(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Usuário id=%s não encontrado", id)
                )
        );
    }

    /* ============================
       LISTAR TODOS
       ============================ */
    @Transactional(readOnly = true)
    public List<AppUser> searchAllUsers() {
        return userRepository.findAll();
    }

    /* ============================
       DELETAR USUÁRIO
       ============================ */
    @Transactional
    public void deleteUser(UUID id) {
        AppUser user = findUserId(id);
        userRepository.delete(user);
    }

    /* ============================
       ATUALIZAR SENHA
       ============================ */
    @Transactional
    public void updatePassword(
            String oldPassword,
            String newPassword,
            String confirmPassword,
            UUID id
    ) {

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("Nova senha não confere com a confirmação");
        }

        AppUser user = findUserId(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordInvalidException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    /* ============================
       BUSCAR POR EMAIL
       ============================ */
    @Transactional(readOnly = true)
    public AppUser buscarPorEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Usuário com email '%s' não encontrado", email)
                )
        );
    }

    /* ============================
       BUSCAR ROLE POR EMAIL
       ============================ */
    @Transactional(readOnly = true)
    public AppUser.Role buscarRolePorEmail(String email) {
        return userRepository.findRoleByEmail(email);
    }
}
