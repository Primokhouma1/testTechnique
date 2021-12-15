package tech.bgdigital.online.payment.services.manager.orabank;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tech.bgdigital.online.payment.models.dto.bankservice.CardDebitIn;
import tech.bgdigital.online.payment.models.entity.*;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.enumeration.Status;
import tech.bgdigital.online.payment.models.enumeration.TypeOperation;
import tech.bgdigital.online.payment.models.repository.*;
import tech.bgdigital.online.payment.services.helper.generator.RandomString;
import tech.bgdigital.online.payment.services.helper.validator.ValidatorBean;
import tech.bgdigital.online.payment.services.http.response.InternalResponse;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;
import tech.bgdigital.online.payment.services.manager.orabank.dto.ErrorMessage;
import tech.bgdigital.online.payment.services.manager.orabank.dto.OraPaymentResponse;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Request3dsAuth;
import tech.bgdigital.online.payment.services.manager.orabank.dto.Response3dsAuth;
import tech.bgdigital.online.payment.services.properties.Environment;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
@Slf4j
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
    @Autowired
    ValidatorBean validatorBean;

    ObjectMapper objectMapper = new ObjectMapper();
    public static String APP_KEY ="app-key";
    public static String SECRETE_KEY ="secrete-key";
    public static String SERVICE_CODE ="ORA_BANK_CARD";
    public ResponseApi<Object> debitCard(CardDebitIn cardDebitIn, HttpServletRequest request){
        ResponseApi<Object>  responseApi = new ResponseApi<>();

        responseApi.message= "Transaction initié avec Succès. Valider l'authentification 3DS";
        responseApi.code = 200;
        responseApi.error = false;
        responseApi.data = new Object();
        Partner partner = this.getPartner(request);
        if(partner== null){
            responseApi.code = 403;
            responseApi.message= "Partenaire est introuvable";
            responseApi.error = true;
            responseApi.data = "";
        }else {
            InternalResponse<Map<String,Object>> validator = validationPayment(cardDebitIn, partner);
            if(validator.error){
                responseApi.message= validator.message;
                responseApi.code = 400;
                responseApi.error = true;
                responseApi.data = validator.response;
                return responseApi;
            }
            InternalResponse<Transaction> responseInit =  this.initTransaction(cardDebitIn,partner);
            if(responseInit.error){
                responseApi.code = 500;
                responseApi.message= responseInit.message;
                responseApi.error = true;
                responseApi.data = null;
                this.finishTransaction(responseInit.response,responseInit.message);
            }else {
                Transaction transaction = responseInit.response;
                Map<String, String> response = new HashMap<>();
                response.put("status",transaction.getStatus());
                response.put("transactionID",transaction.getTrxRef());
                response.put("transactionNumber",transaction.getPartenerTrxRef());
                response.put("url3ds",environment.platformUrl + "/payment/card/redirect/3ds/" + transaction.getTrxRef() + "/authentification-request"   );
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
        Transaction transaction = new Transaction();
        try {
            Service service = serviceRepository.findByCode(OraBankManager.SERVICE_CODE);
            TarifFrai tarifFrai = tarifFraiRepository.findByServicesAndPartners(service,partner);
            transaction.setAmountTrx(cardDebitIn.amount);
            transaction.setProccess(false);
            transaction.setCallBackRetryNumber(0);
            transaction.setCallbackUrl(cardDebitIn.callBackUrl);
            transaction.setRedirectUrl(cardDebitIn.redirectUrl);
            transaction.setCustomerAddress(cardDebitIn.customerAddress);
            transaction.setStatusCallback(Status.INIT);
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
            transaction.setStatus(Status.INIT);
            transaction.setTypeOperation(TypeOperation.CREDIT);
            transaction.setPartners(partner);
            transaction.setServices(service);
            transaction =  transactionRepository.save(transaction);
            //info pay
            transaction.setCustomerCardExpiry(cardDebitIn.customerExpiredCard);
            transaction.setCustomerCardCardholderName(cardDebitIn.customerCardholderName);
            transaction.setCustomerCardPan(cardDebitIn.customerPan);
            transaction.setCustomerPhone(cardDebitIn.customerPhone);

            //todo save transaction
            //todo call paymentRequest
            InternalResponse<OraPaymentResponse> restApiPayement = oraBankIntegration.payment(cardDebitIn,transaction);
            OraPaymentResponse oraPaymentResponse = restApiPayement.response;
            log.debug("ORABANK-PAYMENT-RESPONSE->,{}",objectMapper.writeValueAsString(restApiPayement));
            transaction.setResponseJson(objectMapper.writeValueAsString(restApiPayement));
            if(restApiPayement.error){
                log.error("ERROR API PAYMENT =>{}",restApiPayement);
                //finishTransaction(transaction,restApiPayement.message);
                transactionRepository.save(transaction);
                return new InternalResponse<>(transaction,true,restApiPayement.message);
            }else {
                transaction.setStatus(Status.getState(oraPaymentResponse.state));
                log.info("STATE ORA REQUEST=>{}",oraPaymentResponse.state);
                log.info("STATE ORA=>{}",transaction.getStatus());
                transaction.setCustomerCardType(oraPaymentResponse.paymentMethod.name);
                transaction.setCustomerCardPan(oraPaymentResponse.paymentMethod.pan);
                transactionRepository.save(transaction);
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
//                transaction.setCustomerCardExpiry(oraPaymentResponse.paymentMethod.expiry);
//                transaction.setCustomerCardCardholderName(oraPaymentResponse.paymentMethod.cardholderName);

                if(!Objects.equals(transaction.getStatus(), Status.PENDING) && !Objects.equals(transaction.getStatus(), Status.SUCCESS)){
                    log.info("ORABANK-PAYMENT=> {}",objectMapper.writeValueAsString(oraPaymentResponse));
                    StringBuilder msg= new StringBuilder();
                    if(!(oraPaymentResponse.message == null) ){
                        log.error("ERROR VALIDATION PAIEMENT=>{}",objectMapper.writeValueAsString(oraPaymentResponse.errors));
                        for (ErrorMessage errorMessage:
                             oraPaymentResponse.errors) {
                            switch (errorMessage.message){
                                case "must be a date in the present or in the future":
                                    msg.append("La date d'expiration invalide").append(". ");
                                    break;
                                case "invalid credit card number":
                                    msg.append("Numéro de carte de crédit invalide").append(". ");
                                    break;
                                default:
                                    msg.append(errorMessage.message).append(". ");
                            }

                        }

                    }else {
                         msg = new StringBuilder(oraPaymentResponse.ora3ds.summaryText);
                        if(Objects.equals(oraPaymentResponse.ora3ds.summaryText, "Authentication was attempted but was not or could not be completed; possible reasons being either the card or its Issuing Bank has yet to participate in 3DS.")){
                            msg = new StringBuilder("L'authentification a été tentée mais n'a pas été ou n'a pas pu être effectuée ; les raisons possibles étant que la carte ou sa banque émettrice n'a pas encore participé à 3DS");
                        }
                        if(Objects.equals(oraPaymentResponse.ora3ds.summaryText, "3DS authentication was attempted but was not or could not be completed; possible reasons being either the card or its Issuing Bank has yet to participate in 3DS, or cardholder ran out of time to authorize")){
                            msg = new StringBuilder("L'authentification 3DS a été tentée mais n'a pas été ou n'a pas pu être effectuée ; les raisons possibles étant que la carte ou sa banque émettrice n'a pas encore participé à 3DS, ou que le titulaire de la carte n'a pas eu le temps d'autoriser.");
                        }
                    }


                    return new InternalResponse<>(transaction ,true, msg.toString());
                }
                log.info("SUCCESS INIT TRANSACTION");
                return new InternalResponse<>(transaction,false,"");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            // finishTransaction(transaction);
            log.error("ERROR CACHED INIT PAYMENT {}",exceptionAsString);
            return new InternalResponse<>(transaction ,true,e.getMessage() == null ? "Une erreur est survenue" : e.getMessage() );
        }
    }
    public void finishTransaction(Transaction transaction){
        if(transaction.getId() != null){
            transaction.setStatus(Status.FAILED);
            transaction.setFailedAt(new Date());
            transaction.setMessageError("Transaction annulé");
            transactionRepository.save(transaction);
        }
        new InternalResponse<>(transaction, true, "Transaction annulé");
    }
    public void finishTransaction(Transaction transaction,String message){
        if(transaction.getId() != null){
            transaction.setStatus(Status.FAILED);
            transaction.setFailedAt(new Date());
            transaction.setMessageError(message);
            transactionRepository.save(transaction);
        }
        new InternalResponse<>(transaction, true, message);
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
    public Request3dsAuth getRequest3dsAuthentification(String token){
        Request3dsAuth request3DsAuth = new Request3dsAuth();
        Transaction transaction = transactionRepository.findByTrxRef(token);
        if(transaction==null){
            return  null;
        }
        try {
            request3DsAuth.AcsUrl = transactionItemRepository.findByNameAndTransactions(environment.oraAcsUrl,transaction).getValue();
            request3DsAuth.PaReq =transactionItemRepository.findByNameAndTransactions(environment.oraAcsPaReq,transaction).getValue();
            request3DsAuth.MD =transactionItemRepository.findByNameAndTransactions(environment.oraAcsMd,transaction).getValue();
            request3DsAuth.TermUrl =environment.platformUrl +  environment.oraInterceptorUrl3ds + "/" + token;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        return request3DsAuth;
    }
    public Transaction getTransactionBYMd(String MD,String token){
        Transaction transaction= transactionRepository.findByTrxRef(token);
        TransactionItem transactionItem = transactionItemRepository.findByValueAndNameAndTransactions(MD,environment.oraAcsMd,transaction);
        if(transactionItem == null){
            return  null;
        }
        return  transactionItem.getTransactions();
    }

    public Transaction validate3ds(Response3dsAuth response3dsAuth, String token){
        Transaction transaction = this.getTransactionBYMd(response3dsAuth.MD,token);
        InternalResponse<OraPaymentResponse> internalResponse=  oraBankIntegration.validation3dsApi(response3dsAuth,transaction);
        if(!internalResponse.error){
            transaction.setStatus(Status.getState(internalResponse.response.state));
            transaction.setCallBackRetryNumber(1);
            if(Objects.equals(transaction.getStatus(), Status.SUCCESS)){
                transaction.setSuccessAt(new Date());
                transactionRepository.save(transaction);
            }else if(Objects.equals(transaction.getStatus(), Status.FAILED)){
                transaction.setFailedAt(new Date());
                transactionRepository.save(transaction);
                if(!internalResponse.response.ora3ds.status.isEmpty()){
                    transaction.setMessageError( "Failed card debit");
                    transaction.setMessageAuth3ds( internalResponse.response.ora3ds.status);
                }else {
                    transaction.setMessageError("Failed card debit");
                }
                transactionRepository.save(transaction);
            }
            //Callback
                InternalResponse<String> resCallback =  this.oraBankIntegration.callBackSend(transaction);
                if(!resCallback.error ){
                    transaction.setCallbackSentedAt(new Date());
                    transaction.setCallbackSended(true);
                    transaction.setMessageError("Transaction validé.");
                    transactionRepository.save(transaction);
                    transaction.setStatusCallback(Status.SUCCESS);
                    log.info("SUCCESS CALLBACK");
                }else {
                    transaction.setCallbackFailedAt(new Date());
                    transaction.setCallbackSended(true);
                    transaction.setCallbackMessageError(resCallback.message);
                    transaction.setStatusCallback(Status.FAILED);
                    transactionRepository.save(transaction);
                    log.info("FAILED CALLBACK");
                }
        }
        return transaction;
    }
    public InternalResponse<Map<String,Object>> validationPayment(CardDebitIn cardDebitIn,Partner partner){
        InternalResponse<Map<String, Object>> internalResponse = new InternalResponse<>();
        Map<String, Object> validations= new HashMap<>();
        boolean error= false;
        if(!validatorBean.isAmount(cardDebitIn.amount)){
            error =true;
            validations.put("amount","Le montant doit être supérieur a 0");
        }
        if(!validatorBean.isSecureUrl(cardDebitIn.callBackUrl)){
            error =true;
            validations.put("callBackUrl","L'URL du callback n'est pas valide et il doit êttre en https");
        }
        if(!validatorBean.isUrl(cardDebitIn.redirectUrl)){
            error =true;
            validations.put("redirectUrl","L'URL de redirection n'est pas valide");
        }
        if(!validatorBean.isFullName(cardDebitIn.customerCardholderName)){
            error =true;
            validations.put("customerCardholderName","Le nom est invalide");
        }
        if(!validatorBean.isCvv(cardDebitIn.customerCvv)){
            error =true;
            validations.put("customerCvv","Le CVV  n'est pas valide. Format: 123");
        }
        if(!validatorBean.isExpireDate(cardDebitIn.customerExpiredCard)){
            error =true;
            validations.put("customerExpiredCard","La date d'expiration n'est pas valide. Format : 2030-03");
        }
//        if(!validatorBean.isAddress(cardDebitIn.customerAddress)){
//            error =true;
//            validations.put("customerAddress","L'adresse n'est pas valide.");
//        }
        if(!validatorBean.isPan(cardDebitIn.customerPan)){
            error =true;
            validations.put("customerPan","Le numéro de la carte n'est pas valide. Cartes acceptés : Visa, MasterCard, American Express, Diners Club, Discover, and JCB cards");
        }
        if(validatorBean.isExisteTransactionNumber(cardDebitIn.transactionNumber,partner)){
            error =true;
            validations.put("transactionNumber","Le numéro de transaction existe ");
        }
        if(!validatorBean.isValidPhone(cardDebitIn.customerPhone)){
            error =true;
            validations.put("customerPhone","Le numéro de telephone n'est pas valide ");
        }
        internalResponse.error = error;
        if(error){
            internalResponse.message = "Paramètres non validate";
        }
        internalResponse.response = validations;
        return internalResponse;
    }

    public Boolean processTransaction(Transaction transaction){
        try {
            if(transaction ==null){
                return false;
            }
            transaction.setProccess(true);
            transaction.setProccessAt(new Date());
            transactionRepository.save(transaction);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Transaction getTransaction(String refTrx){
       return this.transactionRepository.findTransactionByTrxRef(refTrx);
    }



}
