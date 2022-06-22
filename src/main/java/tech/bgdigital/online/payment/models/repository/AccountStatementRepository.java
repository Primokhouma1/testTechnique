package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface AccountStatementRepository extends JpaRepository<AccountStatement, Integer>/*, JpaSpecificationExecutor<AccountStatement> */{
    Optional<AccountStatement> findById(Integer id);
    Page<AccountStatement> findAllByStateIsNot(String state, Pageable pageable);
    List<AccountStatement> findAllByStateNot(String state);
    AccountStatement findByIdAndStateNot(Integer id, String state);
    List<AccountStatement> findAccountStatementByAmount(String amount);
}
