package tech.bgdigital.online.payment.services.manager.orabank;

import org.springframework.beans.factory.annotation.Autowired;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Service;
import tech.bgdigital.online.payment.models.entity.TarifFrai;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.repository.PartnerRepository;
import tech.bgdigital.online.payment.models.repository.ServiceRepository;
import tech.bgdigital.online.payment.models.repository.TarifFraiRepository;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;

public class OraBankManager implements OraBankServiceInterface {

   @Autowired PartnerRepository partnerRepository;
   @Autowired
   ServiceRepository serviceRepository;
    @Autowired
    TarifFraiRepository tarifFraiRepository;
    public static String APP_KEY ="app-key";
    public static String SECRETE_KEY ="secrete-key";
    public static String SERVICE_CODE ="ORA_BANK_CARD";

    public ResponseApi<Object> debitCard(CardDebitIn cardDebitIn, HttpServletRequest request){
        ResponseApi<Object>  responseApi = new ResponseApi<Object>();
        responseApi.message= "Transaction initié avec Succès. Valider l'authentification 3DS";
        responseApi.code = 200;
        responseApi.error = false;
        Partner partner = this.getPartner(request);
        if(partner== null){
            responseApi.code = 403;
            responseApi.message= "Partenaire est introuvable";
            responseApi.error = true;
            responseApi.data = "";
        }else {
            responseApi.data = partner;
        }
        return responseApi;
    }
    public Partner getPartner(HttpServletRequest request){
       return partnerRepository.findByAppKeyAndSecreteKey(request.getHeader(OraBankManager.APP_KEY), request.getHeader(OraBankManager.SECRETE_KEY));
    }
    public Transaction initTransaction(CardDebitIn cardDebitIn,Partner partner){
        Service service = serviceRepository.findByCode(OraBankManager.SERVICE_CODE);
        TarifFrai tarifFrai = tarifFraiRepository.findByServicesAndPartners(service,partner);
        Transaction transaction = new Transaction();
        transaction.setAmountTrx(cardDebitIn.amount);
        transaction.setCallBackRetryNumber(0);
        transaction.setCallbackUrl(cardDebitIn.callBackUrl);
        transaction.setCallbackJson("");//todo set data callback json
        transaction.setCallbackSended(false);
        transaction.setCommAmountFixePlateform( tarifFrai.getFeeFixedPartner().subtract(service.getCommFixePsp()) );
        transaction.setCommAmountPercentPlateform(
                cardDebitIn.amount.multiply(tarifFrai.getFeePercentPartner(),new MathContext(4)).divide(new BigDecimal(100))
        );

        return transaction;
    }
    public Transaction finishTransaction(){
        return new Transaction();
    }


}
