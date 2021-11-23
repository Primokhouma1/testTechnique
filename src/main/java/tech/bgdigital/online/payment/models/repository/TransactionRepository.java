package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}