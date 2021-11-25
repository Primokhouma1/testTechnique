package tech.bgdigital.online.payment.models.dto.bankservice;

import java.math.BigDecimal;

public class CardDebitIn {
    public String service;
    public String transactionNumber;
    public BigDecimal amount;
    public String callBackUrl;
    public String customerCardholderName;
    public String customerAddress;
    public String customerCvv;
    public String customerExpiredCard;
    public String customerPan;
}
