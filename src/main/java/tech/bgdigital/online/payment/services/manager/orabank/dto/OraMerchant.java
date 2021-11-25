package tech.bgdigital.online.payment.services.manager.orabank.dto;

public class OraMerchant {
    public String redirectUrl;
    public boolean skipConfirmationPage = false;
    public boolean skip3DS = false;
    public String cancelUrl;
    public String cancelText;
}
