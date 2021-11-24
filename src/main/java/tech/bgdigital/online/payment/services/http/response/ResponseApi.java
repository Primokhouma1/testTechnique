package tech.bgdigital.online.payment.services.http.response;

public class ResponseApi<T> {
    public Integer code;
    public String message;
    public Boolean error;
    public T data;
}
