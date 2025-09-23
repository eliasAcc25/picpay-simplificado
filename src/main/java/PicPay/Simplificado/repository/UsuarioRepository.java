package PicPay.Simplificado.repository;

import PicPay.Simplificado.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCpfOuCnpj(String cnpjCpf);
    boolean existsByEmail(String email);
    boolean existsByCpfOuCnpj(String cnpjCpf);
}
