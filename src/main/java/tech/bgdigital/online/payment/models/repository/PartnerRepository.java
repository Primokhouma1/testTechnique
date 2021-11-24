package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bgdigital.online.payment.models.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
 public Partner findByAppKeyAndSecreteKey(String appKey, String secreteKey);
 public static String APP_KEY ="app-key";
 public static String SECRETE_KEY ="secrete-key";
}