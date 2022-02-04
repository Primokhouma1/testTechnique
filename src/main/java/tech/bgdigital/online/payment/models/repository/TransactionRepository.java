package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.TarifFrai;
import tech.bgdigital.online.payment.models.entity.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
   public Transaction findByTrxRef(String trxRef);
   public Transaction findTransactionByTrxRef(String trxRef);
   public Transaction findTransactionByPartenerTrxRefAndPartners(String trxRef, Partner partner);
   Page<Transaction> findAllByStateIsNot(String state, Pageable pageable);
   List<Transaction> findAllByStateNot(String state);
   Transaction findByIdAndStateNot(Integer id, String state);
   List<Transaction> findTransactionByAmountTrx(String amount_trx);
}
