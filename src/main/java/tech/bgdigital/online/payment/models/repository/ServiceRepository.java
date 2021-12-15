package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
   public Service findByCode(String code);
   Optional<Service> findById(Integer id);
   Page<Service> findAllByStateIsNot(String state, Pageable pageable);
   List<Service> findAllByStateNot(String state);
   Service findByIdAndStateNot(Integer id, String state);
}