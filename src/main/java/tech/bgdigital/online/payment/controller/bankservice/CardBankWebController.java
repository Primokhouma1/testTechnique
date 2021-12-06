package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.enumeration.Status;
import tech.bgdigital.online.payment.models.repository.TransactionRepository;
import tech.bgdigital.online.payment.services.manager.orabank.OraBankServiceInterface;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Request3dsAuth;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Response3dsAuth;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@RequestMapping("payment/card/redirect")
@Api(tags = "Page web Paiement par carte",description = ".")
public class CardBankWebController {
    final
    OraBankServiceInterface oraBankManager;
    final
    TransactionRepository transactionRepository;

    public CardBankWebController(OraBankServiceInterface oraBankManager, TransactionRepository transactionRepository) {
        this.oraBankManager = oraBankManager;
        this.transactionRepository = transactionRepository;
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
            view.addObject("request3dsAuth",request3dsAuth);
            return view;
        }
    }
    @PostMapping(value = "3ds/validation-request/{token}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiOperation(value = "Cette page permet de récupérer le résultat de l'authentification 3DS depuis la banque du client.", hidden = false)
   public void intercept3DSValidation(@PathVariable("token") String token, @RequestBody MultiValueMap<String,String> paramMap, HttpServletResponse httpServletResponse){
        Response3dsAuth response3dsAuth = new Response3dsAuth();
        response3dsAuth.PaRes = paramMap.get("PaRes").get(0);
        response3dsAuth.MD =  paramMap.get("MD").get(0);
        System.out.println("TOKEN =>"+token);
        System.out.println("AAAA =>"+response3dsAuth.MD);
        System.out.println("AAAA =>"+response3dsAuth.PaRes);
        Transaction transaction = oraBankManager.validate3ds(response3dsAuth,token);
        //{"_id":"urn:payment:cd52aa7d-a152-45c1-a4fc-249538302178","_links":{"self":{"href":"https://api-gateway.sandbox.orabank.ngenius-payments.com/transactions/outlets/d6587b88-96d6-4782-95f4-afcbc1420789/orders/5e05134b-4580-4475-8554-762073d027dc/payments/cd52aa7d-a152-45c1-a4fc-249538302178"},"cnp:cancel":{"href":"https://api-gateway.sandbox.orabank.ngenius-payments.com/transactions/outlets/d6587b88-96d6-4782-95f4-afcbc1420789/orders/5e05134b-4580-4475-8554-762073d027dc/payments/cd52aa7d-a152-45c1-a4fc-249538302178/cancel"},"curies":[{"name":"cnp","href":"https://api-gateway.sandbox.orabank.ngenius-payments.com/docs/rels/{rel}","templated":true}]},"paymentMethod":{"expiry":"2030-03","cardholderName":"Pape Samba","name":"VISA","pan":"401200******1112"},"state":"PURCHASED","amount":{"currencyCode":"XOF","value":1500},"updateDateTime":"2021-11-26T17:51:17.936Z","outletId":"d6587b88-96d6-4782-95f4-afcbc1420789","orderReference":"5e05134b-4580-4475-8554-762073d027dc","authResponse":{"authorizationCode":"AB0012","success":true,"resultCode":"00","resultMessage":"Successful approval/completion or that VIP PIN verification is valid","mid":"2039000044","rrn":"000000123456"},"3ds":{"status":"SUCCESS","eci":"05","eciDescription":"Card holder authenticated","summaryText":"The card-holder has been successfully authenticated by their card issuer."}}
        if(Objects.equals(transaction.getStatus(), Status.SUCCESS)){
            httpServletResponse.setHeader("Location", transaction.getRedirectUrl()  );
            httpServletResponse.setStatus(302);
        }
        else {
            System.out.println("OK");
            httpServletResponse.setHeader("Location", " http://localhost:8080/payment/card/redirect/failed-transaction/"+ transaction.getTrxRef() );
            httpServletResponse.setStatus(302);
        }
    }

    /*@RequestMapping(value = "3ds/token-not-working/404",method = RequestMethod.GET)
    @ApiOperation(value = "", hidden = false)
    String redirectTokenNotWorking( HttpServletResponse httpServletResponse){
        return "bankservice/3ds-invalid-token";
    }*/
    @RequestMapping(value = "failed-transaction/{token}",method = RequestMethod.GET)
    @ApiOperation(value = "", hidden = false)
    ModelAndView faildedTransaction(@PathVariable("token") String token, HttpServletResponse httpServletResponse){
        ModelAndView modelAndView = new ModelAndView("bankservice/failed-transaction");
        Transaction transaction = this.oraBankManager.getTransaction(token) ;
        System.out.println("OUT"+token);
        boolean process = false;
        if(transaction != null){
            process = transaction.getProccess();
        }
        this.oraBankManager.processTransaction(transaction);
        modelAndView.addObject("transaction",transaction);
        modelAndView.addObject("process",process);
        return modelAndView;
    }
}
