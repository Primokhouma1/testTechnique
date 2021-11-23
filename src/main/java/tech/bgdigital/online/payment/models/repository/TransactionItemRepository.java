package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.TransactionItem;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Integer> {
}