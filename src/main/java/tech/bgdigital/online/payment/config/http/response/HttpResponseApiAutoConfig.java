package tech.bgdigital.online.payment.config.http.response;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApi;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

@Configuration
@ConditionalOnClass(HttpResponseApiInterface.class)
public class HttpResponseApiAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    HttpResponseApiInterface httpResponseApi(){
        return new HttpResponseApi();
    }
}
