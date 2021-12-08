package tech.bgdigital.online.payment.services.http.response;
import org.springframework.data.domain.Page;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseApi implements HttpResponseApiInterface{

    Map<String, Object> response = new HashMap<>();
    Map<String, Object> responsePaginate
            = new HashMap<>();

    public Map<String, Object> response(Object data , Object code, Boolean error, String message){
        response.put("data", data);
        response.put("code", code == null ? 201 : code);
        response.put("error", error);
        response.put("message", message == null ? "Donnée(s) disponible" : ( message.equals("") ? "Donnée(s) disponible" : message));
        return  response ;
    }
    public Map<String, Object> paginate(Object data, Page pageTuts ){
        responsePaginate.put("content", data);
        responsePaginate.put("currentPage", pageTuts.getNumber());
        responsePaginate.put("totalItems", pageTuts.getTotalElements());
        responsePaginate.put("totalPages", pageTuts.getTotalPages());
        return responsePaginate;
    }
    public  ResponseApi<Object> responseRest(Object data , Integer code, Boolean error, String message) {
        ResponseApi<Object> responseApi = new ResponseApi<>();
        responseApi.data = data ;
        responseApi.code = code == null ? 201 : code;
        responseApi.error = error;
        responseApi.message = message == null ? "Donnée(s) disponible" : ( message.equals("") ? "Donnée(s) disponible" : message);
        return responseApi;
    }

}
