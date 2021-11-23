package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}