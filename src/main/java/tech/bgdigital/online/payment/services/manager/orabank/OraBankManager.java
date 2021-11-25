package tech.bgdigital.online.payment.services.manager.orabank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.*;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.enumeration.Status;
import tech.bgdigital.online.payment.models.enumeration.TypeOperation;
import tech.bgdigital.online.payment.models.repository.*;
import tech.bgdigital.online.payment.services.helper.generator.RandomString;
import tech.bgdigital.online.payment.services.http.response.InternalResponse;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;
import tech.bgdigital.online.payment.services.manager.orabank.dto.OraPaymentResponse;
import tech.bgdigital.online.payment.services.manager.orabank.dto.StatusOraRestOut;
import tech.bgdigital.online.payment.services.properties.Environment;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OraBankManager implements OraBankServiceInterface {

    @Autowired
    PartnerRepository partnerRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    TarifFraiRepository tarifFraiRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    OraBankIntegration oraBankIntegration;
    @Autowired
    TransactionItemRepository transactionItemRepository;
    @Autowired
    Environment environment;

    ObjectMapper objectMapper = new ObjectMapper();
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
            InternalResponse<Transaction> responseInit =  this.initTransaction(cardDebitIn,partner);
            if(responseInit.error){
                responseApi.code = 403;
                responseApi.message= responseInit.message;
                responseApi.error = true;
                responseApi.data = "";
            }else {
                Transaction transaction = responseInit.response;
                Map<String, String> response = new HashMap<>();
                response.put("status",transaction.getStatus());
                response.put("transactionID",transaction.getTrxRef());
                response.put("transactionNumber",transaction.getPartenerTrxRef());
                response.put("3ds",environment.platformUrl + "/payment/card-redirect/3ds/" + transaction.getTrxRef() + "/validation-request"   );
                responseApi.data = response;
            }

        }
        return responseApi;
    }
    public Partner getPartner(HttpServletRequest request){
       return partnerRepository.findByAppKeyAndSecreteKey(request.getHeader(OraBankManager.APP_KEY), request.getHeader(OraBankManager.SECRETE_KEY));
    }
    //https://www.tutorialspoint.com/java/math/bigdecimal_divide_rdroundingmode_scale.htm
    public InternalResponse<Transaction> initTransaction(CardDebitIn cardDebitIn, Partner partner){
        try {
            Service service = serviceRepository.findByCode(OraBankManager.SERVICE_CODE);
            TarifFrai tarifFrai = tarifFraiRepository.findByServicesAndPartners(service,partner);
            Transaction transaction = new Transaction();
            transaction.setAmountTrx(cardDebitIn.amount);
            transaction.setCallBackRetryNumber(0);
            transaction.setCallbackUrl(cardDebitIn.callBackUrl);
          //  transaction.setCallbackJson("");//todo set data callback json
            transaction.setCallbackSended(false);
            /*Set Platform Val*/
            transaction.setCommAmountFixePlateform( tarifFrai.getFeeFixedPartner().subtract(service.getCommFixePsp()) );
            transaction.setCommAmountPercentPlateform(
                    cardDebitIn
                            .amount
                            .multiply(tarifFrai.getFeePercentPartner().subtract(service.getCommPercentPsp()),new MathContext(4))
                            .divide(new BigDecimal("100.0"),4, RoundingMode.CEILING)
            );
            transaction.setCommPercentPlateform( tarifFrai.getFeePercentPartner().subtract(service.getCommPercentPsp()) );
            transaction.setPartAmountPlateform(
                    transaction.getCommAmountFixePlateform()
                                    .add( transaction.getCommAmountPercentPlateform())
            );
            /*Set PSP Val*/
            transaction.setCommAmountFixePsp( service.getCommFixePsp() );
            transaction.setCommAmountPercentPsp(
                    cardDebitIn.amount
                            .multiply(service.getCommPercentPsp())
                            .divide(new BigDecimal("100.0"),4, RoundingMode.CEILING)
            );
            transaction.setCommPercentPsp( service.getCommPercentPsp() );
            transaction.setPartAmountPsp(
                    transaction.getCommAmountFixePsp()
                                    .add( transaction.getCommAmountPercentPsp())
            );

            /*Set PARTNER Val*/
            transaction.setFeeAmountFixePartener( tarifFrai.getFeeFixedPartner() );
            transaction.setFeeAmountPercentPartner(
                    cardDebitIn.amount
                            .multiply(tarifFrai.getFeePercentPartner())
                            .divide(new BigDecimal("100.0"),4, RoundingMode.CEILING)
            );
            transaction.setFeePercentPartner( tarifFrai.getFeePercentPartner() );
            transaction.setPartAmountPartner(
                    cardDebitIn
                            .amount
                            .subtract(transaction.getFeeAmountFixePartener()
                                    .add( transaction.getFeeAmountPercentPartner()))
            );
            transaction.setCreatedAt(new Date());
            transaction.setUpdatedAt(new Date());
            transaction.setPartenerTrxRef(cardDebitIn.transactionNumber);//todo change generated
            RandomString gen = new RandomString(20, ThreadLocalRandom.current());
            transaction.setTrxRef(gen.nextString());
            Transaction existedTrx = transactionRepository.findByTrxRef(transaction.getTrxRef());
            while (existedTrx != null){
                transaction.setTrxRef(gen.nextString());
                existedTrx = transactionRepository.findByTrxRef(transaction.getTrxRef());
            }
            transaction.setPartnerName(partner.getName());
            transaction.setRequestJson(objectMapper.writeValueAsString( cardDebitIn));//set request json;
            transaction.setServiceCode(service.getCode());
            transaction.setServiceName(service.getName());
            transaction.setState(State.ACTIVED);
            transaction.setStatus(Status.PENDING);
            transaction.setTypeOperation(TypeOperation.CREDIT);
            transaction.setPartners(partner);
            transaction.setServices(service);
            transactionRepository.save(transaction);
            //todo save transaction
            //todo call paymentRequest
            OraPaymentResponse oraPaymentResponse = oraBankIntegration.payment(cardDebitIn,transaction).response;
            transaction.setStatus(Status.getState(oraPaymentResponse.state));
            transaction.setCustomerCardExpiry(oraPaymentResponse.paymentMethod.expiry);
            transaction.setCustomerCardCardholderName(oraPaymentResponse.paymentMethod.cardholderName);
            transaction.setCustomerCardType(oraPaymentResponse.paymentMethod.name);
            transaction.setCustomerCardPan(oraPaymentResponse.paymentMethod.pan);
            //todo set Transaction item
            List<TransactionItem> transactionItemList = new ArrayList<>();
            transactionItemList.add(new TransactionItem(environment.oraOrderReferenceName,oraPaymentResponse.orderReference,transaction));
            transactionItemList.add(new TransactionItem(environment.oraOutletIdName,oraPaymentResponse.outletId,transaction));
            transactionItemList.add(new TransactionItem(environment.oraPaymentReferenceName,oraPaymentResponse.id,transaction));
            transactionItemList.add(new TransactionItem(environment.oraCnp3dsUrlName,oraPaymentResponse.link.cnp3ds.href,transaction));
            transactionItemList.add(new TransactionItem(environment.oraSelfTrxUrl,oraPaymentResponse.link.self.href,transaction));
            transactionItemList.add(new TransactionItem(environment.oraAcsUrl,oraPaymentResponse.ora3ds.acsUrl,transaction));
            transactionItemList.add(new TransactionItem(environment.oraAcsPaReq,oraPaymentResponse.ora3ds.acsPaReq,transaction));
            transactionItemList.add(new TransactionItem(environment.oraAcsMd,oraPaymentResponse.ora3ds.acsMd,transaction));
            transactionItemList.add(new TransactionItem(environment.oraSummaryText,oraPaymentResponse.ora3ds.summaryText,transaction));
            transactionItemRepository.saveAll(transactionItemList);
            transactionRepository.save(transaction);
            return new InternalResponse<>(transaction,false,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalResponse<>(new Transaction(),false,e.getMessage());
        }
    }
    public InternalResponse<Transaction> finishTransaction(){
        return new InternalResponse<>();
    }
    public Object handleDebit(){
        return  "";
    }
    public String getTokenOraBank(){
        return  "token";
    }
    public Object paymentRequest(){
        return "";
    }


}
