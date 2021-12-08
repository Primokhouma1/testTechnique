package tech.bgdigital.online.payment.services.manager.orabank;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.entity.TransactionItem;
import tech.bgdigital.online.payment.models.repository.TransactionItemRepository;
import tech.bgdigital.online.payment.services.http.response.InternalResponse;
import tech.bgdigital.online.payment.services.manager.orabank.dto.*;
import tech.bgdigital.online.payment.services.properties.Environment;

@Component
public class OraBankIntegration {
    @Autowired
    Environment environment;
    @Autowired
    TransactionItemRepository transactionItemRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    public InternalResponse<LoginOraOut> login() {
        try {
           // System.out.println("environment.oraAppKey"+environment.oraAppKey);
            HttpResponse<String> response = Unirest.post(environment.oraBaseUrl + "/identity/auth/access-token")
                    .header("Content-Type", "application/vnd.ni-identity.v1+json")
                    .header("Authorization", "Basic "+  environment.oraAppKey)
                    .asString();
            System.out.println("Login" + response.getBody());
            LoginOraOut loginOut = objectMapper.readValue(response.getBody(), LoginOraOut.class);
            return new InternalResponse<>(loginOut, false, "Login Réussit");
        } catch (Exception e) {
            System.out.println("Error Login=>"+ e.getMessage());
           // e.printStackTrace();
            return new InternalResponse<>(null, true, e.getMessage());
        }

    }
    public InternalResponse<OraPaymentResponse> payment(CardDebitIn cardDebitIn, Transaction transaction) {
        try {
            OraPaymentOrder oraPaymentOrder = new OraPaymentOrder();
            /*SET ORDER*/
            oraPaymentOrder.order.action = environment.oraActionPayment;
            oraPaymentOrder.order.amount.amount = cardDebitIn.amount ;
            oraPaymentOrder.order.amount.currencyCode = environment.oraCurrency;
            /*SET PAYMENT INFOS*/
            oraPaymentOrder.payment.pan = cardDebitIn.customerPan;
            oraPaymentOrder.payment.cvv = cardDebitIn.customerCvv;
            oraPaymentOrder.payment.expiry = cardDebitIn.customerExpiredCard;
            oraPaymentOrder.payment.cardholderName = cardDebitIn.customerCardholderName;
            /*SET MERCHANT*/
            oraPaymentOrder.language = environment.oraLang;
            oraPaymentOrder.merchantOrderReference = transaction.getTrxRef();
            oraPaymentOrder.merchantAttributes.skip3DS = false;
            oraPaymentOrder.merchantAttributes.skipConfirmationPage = false;
            oraPaymentOrder.merchantAttributes.redirectUrl = cardDebitIn.redirectUrl;
            oraPaymentOrder.merchantAttributes.cancelUrl =  environment.oraActionCancelUrl;;
            oraPaymentOrder.merchantAttributes.cancelText = environment.oraActionCancelText;
            InternalResponse<LoginOraOut> loginOraOutInternalResponse=login();;

           // System.out.println("Okkkkkk");
            if(loginOraOutInternalResponse.error){
                  return new InternalResponse<>(null, true, loginOraOutInternalResponse.message);
            }
            String token = loginOraOutInternalResponse.response.accessToken;

            HttpResponse<String> response = Unirest.post(environment.oraBaseUrl + "/transactions/outlets/"+ environment.oraRefPointVente +"/payment/card")
                    .header("Content-Type", "application/vnd.ni-payment.v2+json")
                   // .header("Accept", "application/vnd.ni-payment.v2+json")
                    .header("Authorization", "Bearer "+  token)
                    .body(objectMapper.writeValueAsString( oraPaymentOrder))
                    .asString();
            //System.out.println("token =>" + token);
            System.out.println("oraPaymentResponse +>" + response.getBody());
            OraPaymentResponse oraPaymentResponse = objectMapper.readValue(response.getBody(), OraPaymentResponse.class);
            return new InternalResponse<>(oraPaymentResponse, false, "");
        } catch (Exception e) {
            System.out.println("Error Payment=>"+e.getMessage());
           // e.printStackTrace();
            return new InternalResponse<>(null, true, e.getMessage());
        }

    }
    public InternalResponse<OraPaymentResponse> validation3dsApi(Response3dsAuth response3dsAuth,Transaction transaction) {
        try {
            InternalResponse<LoginOraOut> loginOraOutInternalResponse=login();;
            String token = loginOraOutInternalResponse.response.accessToken;
            if(loginOraOutInternalResponse.error){
                  return new InternalResponse<>(null, true, loginOraOutInternalResponse.message);
            }
            TransactionItem oraCnp3dsUrlNameValue = transactionItemRepository.findByNameAndTransactions(environment.oraCnp3dsUrlName,transaction);
            if(oraCnp3dsUrlNameValue ==null){
                return new InternalResponse<>(null, true, "Url de validation non valide");
            }
            HttpResponse<String> response = Unirest.post(oraCnp3dsUrlNameValue.getValue())
                    .header("Content-Type", "application/vnd.ni-payment.v2+json")
                   // .header("Accept", "application/vnd.ni-payment.v2+json")
                    .header("Authorization", "Bearer "+  token)
                    .body(objectMapper.writeValueAsString( response3dsAuth))
                    .asString();
            //System.out.println("token =>" + token);
            System.out.println("oraValidationResponse +>" + response.getBody());
            OraPaymentResponse oraPaymentResponse = objectMapper.readValue(response.getBody(), OraPaymentResponse.class);
            return new InternalResponse<>(oraPaymentResponse, false, "");
        } catch (Exception e) {
            System.out.println("Error Validation");
            e.printStackTrace();
            return new InternalResponse<>(null, true, e.getMessage());
        }

    }
    public InternalResponse<String> callBackSend(Transaction transaction) {
        try {
            CallbackPartnerRequest callbackPartnerRequest = new CallbackPartnerRequest();
            callbackPartnerRequest.amount =transaction.getAmountTrx();
            callbackPartnerRequest.status =transaction.getStatus();
            callbackPartnerRequest.transactionID =transaction.getPartenerTrxRef();
            callbackPartnerRequest.transactionNumber =transaction.getTrxRef();
            callbackPartnerRequest.cardType =transaction.getCustomerCardType();
            callbackPartnerRequest.customerName =transaction.getCustomerCardCardholderName();
            callbackPartnerRequest.customerPhone =transaction.getCustomerPhone();
            //todo add info callback
            HttpResponse<String> response = Unirest.post(transaction.getCallbackUrl())
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString( callbackPartnerRequest))
                    .asString();
            //System.out.println("token =>" + token);
            System.out.println("oraValidationResponse +>" + response.getBody());
           // CallbackPartnerResponse callbackPartnerResponse = objectMapper.readValue(response.getBody(), CallbackPartnerResponse.class);
            return new InternalResponse<>(response.getBody(), false, "Callback sent successful");
        } catch (Exception e) {
            System.out.println("Error Validation");
            e.printStackTrace();
            return new InternalResponse<>(null, true, e.getMessage());
        }

    }

}
