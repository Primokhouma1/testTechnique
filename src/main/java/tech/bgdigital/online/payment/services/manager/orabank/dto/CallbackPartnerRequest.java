package tech.bgdigital.online.payment.services.manager.orabank.dto;

import java.math.BigDecimal;

public class CallbackPartnerRequest {
    public String status;
    public String transactionNumber;
    public String transactionID;
    public BigDecimal amount;
    public String cardType;
    public String customerName;
    public String customerPhone;
}
