package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.CallFund;
import tech.bgdigital.online.payment.models.entity.Partner;

import java.util.List;
import java.util.Optional;

public interface CallFundRepository extends JpaRepository<CallFund, Integer> {
    Optional<CallFund> findById(Integer id);
    Page<CallFund> findAllByStateIsNot(String state, Pageable pageable);
    CallFund findByIdAndStateNot(Integer id, String state);
    List<CallFund> findAllByStateNot(String state);
}