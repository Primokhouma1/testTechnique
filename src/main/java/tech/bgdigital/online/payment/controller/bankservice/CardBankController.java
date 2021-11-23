package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
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
@Api(value = "Paiement Par Carte Bancaire")
public class CardBankController {
    final
    HttpResponseApiInterface httpResponseApi;

    public CardBankController(HttpResponseApiInterface httpResponseApiInterface) {
        this.httpResponseApi = httpResponseApiInterface;
    }

    @PostMapping(value = "debit")
    public ResponseEntity<Map<String, Object>> debitCard(@RequestBody CardDebitIn cardDebitIn){
        return new ResponseEntity<>(httpResponseApi.response(cardDebitIn, HttpStatus.OK, false, ""), HttpStatus.OK);
    }
}
