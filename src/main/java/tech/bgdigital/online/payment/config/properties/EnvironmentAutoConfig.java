package tech.bgdigital.online.payment.config.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.bgdigital.online.payment.services.properties.Environment;

@Configuration
@EnableConfigurationProperties(Environment.class)
public class EnvironmentAutoConfig {

}
