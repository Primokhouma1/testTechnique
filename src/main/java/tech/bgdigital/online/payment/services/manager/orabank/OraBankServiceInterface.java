package tech.bgdigital.online.payment.services.manager.orabank;

import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;

import javax.servlet.http.HttpServletRequest;

public interface OraBankServiceInterface {
    public ResponseApi<Object> debitCard(CardDebitIn cardDebitIn, HttpServletRequest request);
}
