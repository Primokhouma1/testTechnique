package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class OraPaymentResponse {
    @JsonProperty("_id")
    public String id;

    @JsonProperty("_links")
    public OraLink link = new OraLink();

    @JsonProperty("paymentMethod")
    public OraPayment paymentMethod;

    @JsonProperty("state")
    public String state;

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

}
