package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;

public interface AccountStatementRepository extends JpaRepository<AccountStatement, Integer> {
}