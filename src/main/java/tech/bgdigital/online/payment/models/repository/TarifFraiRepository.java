package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.TarifFrai;

public interface TarifFraiRepository extends JpaRepository<TarifFrai, Integer> {
}