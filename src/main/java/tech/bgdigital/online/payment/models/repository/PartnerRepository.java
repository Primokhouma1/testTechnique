package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Integer> {
}