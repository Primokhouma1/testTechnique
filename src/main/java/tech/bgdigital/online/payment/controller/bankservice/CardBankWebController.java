package tech.bgdigital.online.payment.controller.bankservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
@Controller("payment/card-redirect")
public class CardBankWebController {
    @RequestMapping(value = "3ds/{token}/validation-request",method = RequestMethod.GET)
    void index(@PathVariable("token") String token, HttpServletResponse httpServletResponse){

        httpServletResponse.setHeader("Location","URL 3D SECURE");
        httpServletResponse.setStatus(302);
        //  }else {
        //     httpServletResponse.setHeader("Location", "/not-foud/404");
        //      httpServletResponse.setStatus(302);
        //   }

    }

    @RequestMapping(value = "3ds-token-wworking//404",method = RequestMethod.GET)
    String redirectTokenNotWorking( HttpServletResponse httpServletResponse){
        return "404";
    }
}
