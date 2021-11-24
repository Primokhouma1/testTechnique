package tech.bgdigital.online.payment.config.manager.orabank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.bgdigital.online.payment.services.manager.orabank.OraBankManager;
import tech.bgdigital.online.payment.services.manager.orabank.OraBankServiceInterface;

@Configuration
@ConditionalOnClass(OraBankServiceInterface.class)
public class OraBankAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    OraBankServiceInterface oraBankManager(){
        return new OraBankManager();
    }
}
