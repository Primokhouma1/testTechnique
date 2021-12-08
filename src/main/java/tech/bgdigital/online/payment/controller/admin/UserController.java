package tech.bgdigital.online.payment.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.models.dto.admin.UserDtoIn;
import tech.bgdigital.online.payment.models.dto.admin.UserDtoOut;
import tech.bgdigital.online.payment.models.repository.UserRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;
import tech.bgdigital.online.payment.services.http.response.ResponseApi;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@Api(tags = "Admin utilisateur",description = ".")
public class UserController {
    final
    HttpResponseApiInterface httpResponseApi;
    final
    UserRepository userRepository;

    public UserController(HttpResponseApiInterface httpResponseApi,UserRepository userRepository) {
        this.httpResponseApi = httpResponseApi;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Liste des utilisateurs")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> index(){
        return new ResponseEntity<>(httpResponseApi.response(userRepository.findAll(), 200, false, "Ok"), HttpStatus.OK);
    }
    @ApiOperation(value = "Ajouter un utilisateur")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> store(@RequestBody UserDtoIn userDtoIn){
        return new ResponseEntity<>(httpResponseApi.response(userDtoIn, 200, false, "Ok"), HttpStatus.OK);
    }
    @ApiOperation(value = "Modifier un utilisateur")
    @PutMapping(value = "{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable("id") Integer id,@RequestBody UserDtoIn userDtoIn){
        return new ResponseEntity<>(httpResponseApi.response(userDtoIn, 200, false, "Ok"), HttpStatus.OK);
    }

    @ApiOperation(value = "Supprimer un utilisateur")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") Integer id,@RequestBody UserDtoIn userDtoIn){
        return new ResponseEntity<>(httpResponseApi.response(userDtoIn, 200, false, "Ok"), HttpStatus.OK);
    }

    @ApiOperation(value = "Voir un utilisateur")
    @GetMapping(value = "{id}")
    public ResponseApi<Object> show(@PathVariable("id") Integer id, @RequestBody UserDtoIn userDtoIn){
        return httpResponseApi.responseRest(new UserDtoOut(), 200, false, "Ok");
    }
}
