package tech.bgdigital.online.payment.services.manager.orabank.dto;

public class OraPaymentOrder {
    public OraOrder order = new OraOrder();
    public OraPayment payment = new OraPayment();
    public String language = "fr";
    public String merchantOrderReference ;
    public OraMerchant merchantAttributes = new OraMerchant();
}
