package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.entity.TransactionItem;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, Integer> {
    public TransactionItem findByNameAndTransactions(String name, Transaction transaction);
    public TransactionItem findByValueAndNameAndTransactions(String value,String name,Transaction transaction);
}