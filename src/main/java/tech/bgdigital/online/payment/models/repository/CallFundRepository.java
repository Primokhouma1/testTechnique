package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.CallFund;

public interface CallFundRepository extends JpaRepository<CallFund, Integer> {
}