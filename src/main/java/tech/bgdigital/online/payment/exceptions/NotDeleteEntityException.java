package tech.bgdigital.online.payment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody()
public class NotDeleteEntityException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    private Map<String, Object> response = new HashMap<>();

    public NotDeleteEntityException(ArrayList data, int code, boolean error, String message){
//        super(response.toString());
//        super(String.format("message: %s", message));

        Map<String, Object> response = new HashMap<>();

        response.put("data",data);
        response.put("code",code);
        response.put("error",error);
        response.put("message",message);
        this.setResponse(response);

    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
