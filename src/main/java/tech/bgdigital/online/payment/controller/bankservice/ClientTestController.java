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
        "amount": 1500,
        "callBackUrl": "https://magento.local:9090/ipn.php",
        "customerAddress": "Dakar Sebegal",
        "customerCardholderName": "Pape Samba",
        "customerCvv": "737",
        "customerExpiredCard": "2030-03",
        "customerPan": "4012001037141112",
        "redirectUrl": "http://localhost:8080/client/success",
        "transactionNumber": "1234322344-2",
        "customerPhone":"777293282"
        }

        {
  "amount": 100,
  "callBackUrl": "https://pdg-dev.com",
  "customerAddress": "string",
  "customerCardholderName": "Pape Samba Ndour",
  "customerCvv": "737",
  "customerExpiredCard": "2023-12",
  "customerPan": "4222222222222",
  "redirectUrl": "https://pdg-dev.com",
  "transactionNumber": "1000200012"
}
      {
            "amount": 100,
            "callBackUrl": "https://localhost:9090/ipn.php",
            "customerAddress": "Dakar Sebegal",
            "customerCardholderName": "Pape Samba",
            "customerCvv": "883",
            "customerExpiredCard": "2022-10",
            "customerPan": "5287350230372585",
            "redirectUrl": "http://localhost:9090/client/success.php",
            "transactionNumber": "1000010004",
            "customerPhone":"777293282"
        }


*/
