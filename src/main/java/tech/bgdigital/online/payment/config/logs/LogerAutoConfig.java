package tech.bgdigital.online.payment.config.logs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.bgdigital.online.payment.services.logs.LogService;
import tech.bgdigital.online.payment.services.logs.LogServiceInterface;

@Configuration
@ConditionalOnClass(LogServiceInterface.class)
public class LogerAutoConfig {
    @Bean
    //////////////////////si on n'a pas de bean deja instancier : syngleton\\\\\\\\\\\\\\\\\\\\\
    @ConditionalOnMissingBean
    public LogServiceInterface logService(){
        return new LogService();
    }
}
