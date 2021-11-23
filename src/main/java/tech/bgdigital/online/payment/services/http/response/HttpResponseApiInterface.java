package tech.bgdigital.online.payment.services.http.response;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public interface HttpResponseApiInterface {
    Map<String, Object> response = new HashMap<>();
    Map<String, Object> responsePaginate
            = new HashMap<>();
    public Map<String, Object> response(Object data , Object code, Boolean error, String message);
    public Map<String, Object> paginate(Object data, Page pageTuts );
}
