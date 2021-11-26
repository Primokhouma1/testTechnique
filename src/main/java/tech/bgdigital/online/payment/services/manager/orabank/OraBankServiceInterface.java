package tech.bgdigital.online.payment.services.manager.orabank;

import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.services.http.response.InternalResponse;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Request3dsAuth;

import javax.servlet.http.HttpServletRequest;

public interface OraBankServiceInterface {
    public ResponseApi<Object> debitCard(CardDebitIn cardDebitIn, HttpServletRequest request);
    public void finishTransaction(Transaction transaction);
    public InternalResponse<Transaction> initTransaction(CardDebitIn cardDebitIn, Partner partner);
    public Partner getPartner(HttpServletRequest request);
    public Request3dsAuth getRequest3dsAuthentification(String token);
    public Transaction getTransactionBYMd(String MD);
}
