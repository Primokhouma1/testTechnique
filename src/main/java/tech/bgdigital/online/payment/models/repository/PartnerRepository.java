package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Partner;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
 public Partner findByAppKeyAndSecreteKey(String appKey, String secreteKey);
 List<Partner> findAllByStateNot(String state);
 public static String APP_KEY ="app-key";
 public static String SECRETE_KEY ="secrete-key";
 Optional<Partner> findById(Integer id);
 Page<Partner> findAllByStateIsNot(String state, Pageable pageable);
 Partner findByIdAndStateNot(Integer id, String state);
}