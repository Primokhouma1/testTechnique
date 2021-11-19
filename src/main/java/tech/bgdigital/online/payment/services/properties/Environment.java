package tech.bgdigital.online.payment.services.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("env")
public class Environment {
    public String oraUrlBase;
}
