package tech.bgdigital.online.payment.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.bgdigital.online.payment.services.properties.Environment;

@EnableConfigurationProperties(Environment.class)
public class EnvironmentAutoConfig {

}
