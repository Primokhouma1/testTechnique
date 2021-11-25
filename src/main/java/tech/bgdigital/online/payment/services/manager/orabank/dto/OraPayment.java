package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OraPayment {
    @JsonProperty("pan")
    public String pan;

    @JsonProperty("expiry")
    public String expiry;

    @JsonProperty("cvv")
    public String cvv;

    @JsonProperty("cardholderName")
    public String cardholderName;

    @JsonProperty("name")
    public String name ="";
}
