package tech.bgdigital.online.payment.models.enumeration;

import tech.bgdigital.online.payment.services.manager.orabank.dto.StatusOraRestOut;

public class Status {
    public final static String  INIT = "INIT";
    public final static String  PENDING = "PENDING";
    public final static String   SUCCESS  = "SUCCESS";
    public final static String  CANCELED = "CANCELED";
    public final static String  FAILED = "FAILED";
    public final static String getState(String statusOra){
        switch (statusOra){
            case StatusOraRestOut
                    .AWAIT_3DS:
                return Status.PENDING;
            case StatusOraRestOut
                    .REVERSED:
                return Status.CANCELED;
            case StatusOraRestOut
                    .PURCHASED:
                return Status.SUCCESS;
            default:
                return Status.FAILED ;
        }
    }
}
