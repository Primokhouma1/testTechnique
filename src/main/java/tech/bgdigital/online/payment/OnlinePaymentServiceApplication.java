package tech.bgdigital.online.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class OnlinePaymentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(OnlinePaymentServiceApplication.class, args
        );
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||-----SERVER-------||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||Documentation => http://localhost:8080/swagger-ui.html ||| API HOME http://localhost:8080                                      |||");
        System.out.println("|||Redirect 3DS => http://localhost:8080/payment/card/redirect/3ds/token/authentification-request      |||");
        System.out.println("|||Redirect 404 => http://localhost:8080/payment/card/redirect/3ds/token-not-working/404      |||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||-----SERVER-------||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    }

}
