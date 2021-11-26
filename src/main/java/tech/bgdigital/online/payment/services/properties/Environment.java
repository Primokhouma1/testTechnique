package tech.bgdigital.online.payment.services.properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties("internalenv")
@Setter
@Getter
public class Environment {
    public String oraBaseUrl;
    public String oraAppKey;
    public String oraRefPointVente;
    public String oraOrderReferenceName;
    public String oraPaymentReferenceName;
    public String oraCnp3dsUrlName;
    public String oraOutletIdName;
    public String oraCurrency;
    public String oraActionPayment;
    public String oraActionCancelUrl;
    public String oraActionCancelText;
    public String oraLang;
    public String oraSelfTrxUrl;
    public String oraAcsUrl;
    public String oraAcsPaReq;
    public String oraAcsMd;
    public String oraSummaryText;
    //platefom
    public String platformUrl;
    public String oraInterceptorUrl3ds;

}
