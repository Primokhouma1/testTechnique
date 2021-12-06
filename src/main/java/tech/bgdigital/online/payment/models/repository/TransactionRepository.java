package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Transaction;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
   public Transaction findByTrxRef(String trxRef);
   public Transaction findTransactionByTrxRef(String trxRef);
   public Transaction findTransactionByPartenerTrxRefAndPartners(String trxRef, Partner partner);
}