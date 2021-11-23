package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}