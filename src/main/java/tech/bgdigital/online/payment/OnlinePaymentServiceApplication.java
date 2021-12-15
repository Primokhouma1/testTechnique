package tech.bgdigital.online.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@Slf4j
public class OnlinePaymentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(OnlinePaymentServiceApplication.class, args
        );
        log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||-----SERVER-------||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        log.info("|||Documentation => http://localhost:8080/swagger-ui.html ||| API HOME http://localhost:8080                                      |||");
        log.info("|||Redirect 3DS => http://localhost:8080/payment/card/redirect/3ds/token/authentification-request      |||");
        log.info("|||Redirect 404 => http://localhost:8080/payment/card/redirect/3ds/token-not-working/404      |||");
        log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||-----SERVER-------||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    }

}
