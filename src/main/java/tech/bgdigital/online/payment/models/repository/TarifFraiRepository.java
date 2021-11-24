package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Service;
import tech.bgdigital.online.payment.models.entity.TarifFrai;

@Repository
public interface TarifFraiRepository extends JpaRepository<TarifFrai, Integer> {
    public TarifFrai findByServicesAndPartners(Service service, Partner partner);
}