package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.Profil;

public interface ProfilRepository extends JpaRepository<Profil, Integer> {
}