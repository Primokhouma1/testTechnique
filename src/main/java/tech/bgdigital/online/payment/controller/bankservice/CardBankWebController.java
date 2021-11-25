package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("payment/card/redirect")
@Api(tags = "Page web Paiement par carte",description = ".")
public class CardBankWebController {
    @RequestMapping(value = "3ds/{token}/validation-request",method = RequestMethod.GET)
    @ApiOperation(value = "Cette page permet de récupérer l'Id de la transaction fournit par le client dans l'URL et de lui générer un lien vers la page 3ds de sa banque", hidden = false)
    void index(@PathVariable("token") String token, HttpServletResponse httpServletResponse){

        httpServletResponse.setHeader("Location","URL 3D SECURE");
        httpServletResponse.setStatus(302);
        //  }else {
        //     httpServletResponse.setHeader("Location", "/not-foud/404");
        //      httpServletResponse.setStatus(302);
        //   }

    }

    @RequestMapping(value = "3ds/token-not-working/404",method = RequestMethod.GET)
    @ApiOperation(value = "", hidden = false)
    String redirectTokenNotWorking( HttpServletResponse httpServletResponse){
        return "404";
    }
}
