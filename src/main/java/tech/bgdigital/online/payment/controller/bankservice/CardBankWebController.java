package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.repository.PartnerRepository;
import tech.bgdigital.online.payment.services.manager.orabank.OraBankServiceInterface;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Request3dsAuth;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Response3dsAuth;

import javax.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("payment/card/redirect")
@Api(tags = "Page web Paiement par carte",description = ".")
public class CardBankWebController {
    final
    OraBankServiceInterface oraBankManager;
    final
    PartnerRepository partnerRepository;

    public CardBankWebController(OraBankServiceInterface oraBankManager, PartnerRepository partnerRepository) {
        this.oraBankManager = oraBankManager;
        this.partnerRepository = partnerRepository;
    }

    @RequestMapping(value = "3ds/{token}/authentification-request",method = RequestMethod.GET)
    @ApiOperation(value = "Cette page permet de récupérer l'Id de la transaction fournit par le client dans l'URL et de lui générer un lien vers la page 3ds de sa banque", hidden = false)
   public ModelAndView redirect3dsAuth(@PathVariable("token") String token, HttpServletResponse httpServletResponse){
        ModelAndView view = new ModelAndView("bankservice/3ds-redirect.html");
        Request3dsAuth request3dsAuth= oraBankManager.getRequest3dsAuthentification(token);
        if(request3dsAuth == null){
            httpServletResponse.setHeader("Location", "/payment/card/redirect/3ds/token-not-working/404");
            return new ModelAndView("bankservice/3ds-invalid-token");
        }else {
            view.addObject("partner",request3dsAuth);
            return view;
        }
    }
    @RequestMapping(value = "3ds/validation-request",method = RequestMethod.POST)
    @ApiOperation(value = "Cette page permet de récupérer le résultat de l'authentification 3DS depuis la banque du client.", hidden = false)
   public String intercept3DSValidation(@RequestBody() Response3dsAuth response3dsAuth, HttpServletResponse httpServletResponse){
        Transaction transaction = oraBankManager.getTransactionBYMd(response3dsAuth.MD);
        return response3dsAuth.PaRes;

    }

    @RequestMapping(value = "3ds/token-not-working/404",method = RequestMethod.GET)
    @ApiOperation(value = "", hidden = false)
    String redirectTokenNotWorking( HttpServletResponse httpServletResponse){
        return "bankservice/3ds-invalid-token";
    }
}
