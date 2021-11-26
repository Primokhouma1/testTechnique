package tech.bgdigital.online.payment.controller.bankservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
public class ClientTestController {

    @GetMapping("/callback")
    public String callBackUrl(){
        return "Success";
    }
    @GetMapping("/success")
    public String redirectSuccess(){
        return "Success";
    }
    @GetMapping("/cancel")
    public String redirectFail(){
        return "Cancel";
    }

}
/*
       {
        "amount": 100,
        "callBackUrl": "http://localhost:8080/client/callback",
        "customerAddress": "Dakar Sebegal",
        "customerCardholderName": "Pape Samba",
        "customerCvv": "737",
        "customerExpiredCard": "2030-03",
        "customerPan": "4012001037141112",
        "redirectUrl": "http://localhost:8080/client/success",
        "transactionNumber": "1234322344"
        }


*/
