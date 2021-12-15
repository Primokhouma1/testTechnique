package tech.bgdigital.online.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class HomeApi {
    @GetMapping("")
    ResponseEntity<Map<String, Object>> home(){
      Map<String,Object>  home = new HashMap<>();
        home.put("name", "PAYMENT BANKING");
        home.put("version", "1.0.0");
        return new ResponseEntity<>(home, HttpStatus.OK);
    }
}
