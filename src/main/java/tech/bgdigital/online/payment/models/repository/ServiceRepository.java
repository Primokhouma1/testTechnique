package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
   public Service findByCode(String code);
}