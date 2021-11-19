package tech.bgdigital.online.payment.config.http.orabank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.bgdigital.online.payment.services.http.orabank.OraBankService;
import tech.bgdigital.online.payment.services.http.orabank.OraBankServiceInterface;

@Configuration
@ConditionalOnClass(OraBankServiceInterface.class)
public class OraBankAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    OraBankServiceInterface oraBankService(){
        return new OraBankService();
    }
}
