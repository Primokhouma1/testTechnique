package tech.bgdigital.online.payment.services.logs;

public interface LogServiceInterface {
    void debug(String tag,String message);
    void error(String tag,String message);
    void infos(String tag,String message);
}
