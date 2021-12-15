package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.bgdigital.online.payment.models.enumeration.Status;

import java.util.ArrayList;
import java.util.List;


public class OraPaymentResponse {
    @JsonProperty("_id")
    public String id;

    @JsonProperty("_links")
    public OraLink link = new OraLink();

    @JsonProperty("paymentMethod")
    public OraPayment paymentMethod;

    @JsonProperty("state")
    public String state = Status.FAILED;

    @JsonProperty("amount")
    public OraAmount amount =  new OraAmount();

    @JsonProperty("updateDateTime")
    public String updateDateTime;

    @JsonProperty("outletId")
    public String outletId;

    @JsonProperty("orderReference")
    public String orderReference;

    @JsonProperty("3ds")
    public Ora3ds ora3ds = new Ora3ds();

    @JsonProperty("authResponse")
    public AuthResponse authResponse = new AuthResponse();

    //
   public String message;
   public String code;
   public List<ErrorMessage> errors ;
    //{"message":"Unprocessable Entity","code":422,"errors":[{"message":"invalid credit card number","localizedMessage":"Invalid Card Number","location":"pan","errorCode":"invalidPan","domain":"processing"}]}

}
