package tech.bgdigital.online.payment.controller.bankservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
public class ClientTestController {

    @GetMapping("/success")
    public String redirectSuccess(){
        return "Success";
    }
    @GetMapping("/cancel")
    public String redirectFail(){
        return "Cancel";
    }

}
