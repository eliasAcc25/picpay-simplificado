package PicPay.Simplificado.repository;

import PicPay.Simplificado.model.entity.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    Optional<Saldo> findByUser_Id(Long userId);
}
