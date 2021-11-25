package tech.bgdigital.online.payment.services.http.response;

public class InternalResponse<T> {
    public boolean error;
    public String message;
    public T response;

    public InternalResponse() {
    }

    public InternalResponse(T response, boolean error, String message) {
        this.error = error;
        this.message = message;
        this.response = response;
    }
}
