package tech.bgdigital.online.payment.controller.bankservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.Map;

@RestController("payment/card")
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
    public CardBankController(HttpResponseApiInterface httpResponseApiInterface, HttpServletRequest request, HttpServletResponse response, OraBankServiceInterface oraBankManager) {
        this.httpResponseApi = httpResponseApiInterface;
        this.request = request;
        this.response = response;
        this.oraBankManager = oraBankManager;
    }

    @ApiOperation(value = "Cette methode permet de débiter une carte visa ou master card d'un client.")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = CardBankController.class /*responseContainer = "List"*/) })
    @PostMapping(value = "debit")
    public ResponseEntity<Map<String, Object>> debitCard(@RequestBody CardDebitIn cardDebitIn){
        ResponseApi<Object> responseApi =  oraBankManager.debitCard(cardDebitIn,request);
        return new ResponseEntity<>(httpResponseApi.response(responseApi.data, responseApi.code, responseApi.error, responseApi.message), HttpStatus.OK);
    }

}
