package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(Integer id);
    Page<User> findAllByStateIsNot(String state, Pageable pageable);
    List<User> findAllByStateNot(String state);
    User findByIdAndStateNot(Integer id, String state);
    List<User> findUsersByEmailOrFirstNameOrLastName(String email, String first_name,  String last_name);
    Optional<User> findByEmail(String id);
}
