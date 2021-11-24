package tech.bgdigital.online.payment.services.manager.orabank;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.bgdigital.online.payment.services.properties.Environment;

@Component
public class OraBankIntegration {
    @Autowired
    Environment environment;
    public String login() {
        try {
            HttpResponse<String> response = Unirest.post(environment.oraUrlBase + "/identity/auth/access-token")
                    .header("Content-Type", "application/vnd.ni-identity.v1+json")
                    .header("Authorization", environment.oraAppKey)
                    .asString();
            return response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return "";
        }

    }
}
