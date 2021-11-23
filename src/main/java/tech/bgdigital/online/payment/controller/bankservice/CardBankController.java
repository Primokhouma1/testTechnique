package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitOut;
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

    @ApiOperation(value = "Cette methode permet de débiter une carte visa ou master card d'un client.")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = CardBankController.class /*responseContainer = "List"*/) })
    @PostMapping(value = "debit")
    public ResponseEntity<Map<String, Object>> debitCard(@RequestBody CardDebitIn cardDebitIn){
        return new ResponseEntity<>(httpResponseApi.response(cardDebitIn, HttpStatus.OK, false, ""), HttpStatus.OK);
    }
}
