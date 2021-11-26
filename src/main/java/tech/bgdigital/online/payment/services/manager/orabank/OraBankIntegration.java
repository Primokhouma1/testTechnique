package tech.bgdigital.online.payment.services.manager.orabank;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.services.http.response.InternalResponse;
import tech.bgdigital.online.payment.services.manager.orabank.dto.LoginOraOut;
import tech.bgdigital.online.payment.services.manager.orabank.dto.OraPaymentOrder;
import tech.bgdigital.online.payment.services.manager.orabank.dto.OraPaymentResponse;
import tech.bgdigital.online.payment.services.properties.Environment;

@Component
public class OraBankIntegration {
    @Autowired
    Environment environment;
    ObjectMapper objectMapper = new ObjectMapper();
    public InternalResponse<LoginOraOut> login() {
        try {
           // System.out.println("environment.oraAppKey"+environment.oraAppKey);
            HttpResponse<String> response = Unirest.post(environment.oraBaseUrl + "/identity/auth/access-token")
                    .header("Content-Type", "application/vnd.ni-identity.v1+json")
                    .header("Authorization", "Basic "+  environment.oraAppKey)
                    .asString();
          //  System.out.println("Login" + response.getBody());
            LoginOraOut loginOut = objectMapper.readValue(response.getBody(), LoginOraOut.class);
            return new InternalResponse<>(loginOut, false, "");
        } catch (Exception e) {
            System.out.println("Error Login");
            e.printStackTrace();
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
            String token = login().response.accessToken;
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
            System.out.println("Error Payment");
            e.printStackTrace();
            return new InternalResponse<>(null, true, e.getMessage());
        }

    }

}
