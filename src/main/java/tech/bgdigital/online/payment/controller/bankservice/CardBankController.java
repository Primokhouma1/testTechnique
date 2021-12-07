package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;
import tech.bgdigital.online.payment.services.manager.orabank.OraBankServiceInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.util.Map;

@RestController
@RequestMapping("/payment/card")
@Api(tags = "Paiement Par Carte Bancaire",description = ".")
@Scope("request")
public class CardBankController {
    final
    HttpResponseApiInterface httpResponseApi;
    final
    HttpServletRequest request;
    final
    HttpServletResponse response;
    final
    OraBankServiceInterface oraBankManager;
    @Autowired
    Validator validator;
    public CardBankController(HttpResponseApiInterface httpResponseApiInterface, HttpServletRequest request, HttpServletResponse response, OraBankServiceInterface oraBankManager) {
        this.httpResponseApi = httpResponseApiInterface;
        this.request = request;
        this.response = response;
        this.oraBankManager = oraBankManager;
    }
    @ApiOperation(value = "Cette methode permet de débiter une carte visa ou master card d'un client.")
    @PostMapping(value = "debit/{service}")
    public  ResponseEntity<Map<String, Object>> debitCard(@RequestBody CardDebitIn cardDebitIn){
        ResponseApi<Object> responseApi = new ResponseApi<>();
        if (ApiService.ORA_BANK.equals(cardDebitIn.service)) {
            responseApi = oraBankManager.debitCard(cardDebitIn, request);
        } else {
            responseApi.data = null;
            responseApi.message = "Service introuvable";
            responseApi.code = 404;
            responseApi.error = true;
        }
       //responseApi =  oraBankManager.debitCard(cardDebitIn,request);
        return new ResponseEntity<>(httpResponseApi.response(responseApi.data, responseApi.code, responseApi.error, responseApi.message), HttpStatus.OK);
    }
    @ApiOperation(value = "Cette methode permet de récupérer le résultat d'une validation 3DS depuis la page d'authentification 3DS de la banque du client.")
    @PostMapping(value = "interceptor/{partnerId}/{token}")
    public ResponseEntity<Map<String, Object>> interceptor(@PathVariable("partnerId") String partnerId,@PathVariable("token") String token){
        return new ResponseEntity<>(httpResponseApi.response(token +" <=> "+ partnerId, 200, false, "Annulation"), HttpStatus.OK);
    }
    @ApiOperation(value = "Cette methode permet d'annuler une transaction et de rembourser le montant débiter.")
    @PutMapping(value = "cancel/{token}")
    public ResponseEntity<Map<String, Object>> cancel(@PathVariable("token") String token){
        return new ResponseEntity<>(httpResponseApi.response(token, 200, false, "Annulation"), HttpStatus.OK);
    }

}
