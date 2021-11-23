package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import java.util.Map;

@RestController
@Api(tags = "Paiement Par Carte Bancaire",description = ".")
public class CardBankController {
    final
    HttpResponseApiInterface httpResponseApi;

    public CardBankController(HttpResponseApiInterface httpResponseApiInterface) {
        this.httpResponseApi = httpResponseApiInterface;
    }

    @PostMapping(value = "debit")
    @ApiOperation(value = "Cette methode permet de débiter une carte visa ou master card d'un client.")
    public ResponseEntity<Map<String, Object>> debitCard(@RequestBody CardDebitIn cardDebitIn){
        return new ResponseEntity<>(httpResponseApi.response(cardDebitIn, HttpStatus.OK, false, ""), HttpStatus.OK);
    }
}
