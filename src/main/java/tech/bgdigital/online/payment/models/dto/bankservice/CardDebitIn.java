package tech.bgdigital.online.payment.models.dto.bankservice;

import java.math.BigDecimal;

public class CardDebitIn {
    public String service;
    public BigDecimal amount;
    public String callBackUrl;
    public String customerFirstName;
    public String customerLastName;
    public String customerAddress;
    public String customerCvv;
    public String customerExpiredCard;
    public String customerExpiredPan;
}
