package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Profil;
import tech.bgdigital.online.payment.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Integer> {
    Optional<Profil> findById(Integer id);
    Page<Profil> findAllByStateIsNot(String state, Pageable pageable);
    List<Profil> findAllByStateNot(String state);
    List<Profil> findProfilsByName(String name);
    Profil findByIdAndStateNot(Integer id, String state);
}
