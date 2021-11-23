package tech.bgdigital.online.payment.services.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("env")
public class Environment {
    public String oraUrlBase;
    public String oraAppKey;
    public String oraRefPointVente;
    public String oraOrderReferenceName;
    public String oraPaymentReferenceName;
    public String oraCnp3dsUrlName;
    public String oraOutletIdName;
}
