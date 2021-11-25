package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
   public Transaction findByTrxRef(String trxRef);
}