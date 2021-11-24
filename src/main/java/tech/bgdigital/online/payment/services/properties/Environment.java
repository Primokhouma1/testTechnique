package tech.bgdigital.online.payment.services.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("env")
@Component
public class Environment {
    public String oraUrlBase;
    public String oraAppKey;
    public String oraRefPointVente;
    public String oraOrderReferenceName;
    public String oraPaymentReferenceName;
    public String oraCnp3dsUrlName;
    public String oraOutletIdName;
}
