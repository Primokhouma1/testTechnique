package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Service;
import tech.bgdigital.online.payment.models.entity.TarifFrai;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifFraiRepository extends JpaRepository<TarifFrai, Integer> {
    public TarifFrai findByServicesAndPartners(Service service, Partner partner);
    Optional<TarifFrai> findById(Integer id);
    Page<TarifFrai> findAllByStateIsNot(String state, Pageable pageable);
    List<TarifFrai> findAllByStateNot(String state);
    TarifFrai findByIdAndStateNot(Integer id, String state);
    List<TarifFrai> findTarifFraiByState(String state);
}
