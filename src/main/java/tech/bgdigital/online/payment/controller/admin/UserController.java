package tech.bgdigital.online.payment.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.exceptions.NotDeleteEntityException;
import tech.bgdigital.online.payment.models.entity.Profil;
import tech.bgdigital.online.payment.models.entity.User;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.UserRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/admin/user")
@Api(tags = "Admin utilisateur",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    final UserRepository userRepository;
    final
    HttpResponseApiInterface httpResponseApi;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Date currrent = new Date();
    public UserController(UserRepository userRepository, HttpResponseApiInterface httpResponseApi) {
        this.userRepository = userRepository;
        this.httpResponseApi = httpResponseApi;
    }

    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des users paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<User> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            pageTuts = userRepository.findAllByStateIsNot(State.DELETED, paging);
            listType = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response = httpResponseApi.paginate(listType, pageTuts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            return new ResponseEntity<>(httpResponseApi.response(null, 500, true,"Ok"), HttpStatus.OK);
        }
    }

    @GetMapping("")
    @ApiOperation(value = "Liste des users")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(userRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details user paginés")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            User user = userRepository.findByIdAndStateNot(id, State.DELETED);
            if (user != null) {
                return httpResponseApi.response(user, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cette région n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }


    /**
     * Desactiver etat a 0
     */
    @GetMapping("/state/{id}")
    @Transactional
    @ApiOperation(value = "Changer etat des users paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            User userExist = userRepository.findByIdAndStateNot(id, State.DELETED);
            if (userExist == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (userExist.getState().equals(State.ACTIVED)) {
                userExist.setState(State.DISABLED);
                message = "etat activé avec succéss";

            } else {
                userExist.setState(State.ACTIVED);
                message = "etat desactivé avec succéss";
            }

            userRepository.save(userExist);
            return httpResponseApi.response(userExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    @PostMapping("")
    @ApiOperation(value = "Ajouter user")
    public Map<String, Object> store(@Valid @RequestBody User user) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/
            System.out.println("user"+user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //System.out.println("user"+user);
            user.setState("ACTIVED");
           // user.setCreatedAt(Date());
            userRepository.save(user);

            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(user, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(user, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  user paginé")
    public Map<String, Object> updateRegion(@Valid @RequestBody Profil profil) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
            User user1 = userRepository.findByIdAndStateNot(profil.getId(), State.DELETED);
            if (user1 == null) {
                return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce user n'existe pas.");
            } else {
                //todo set all value you want to update from accountStatement
                profil.setState(profil.getState());
                userRepository.save(user1);
                return httpResponseApi.response(profil, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
            }
            //}

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }


    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer les users paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            User user = userRepository.findByIdAndStateNot(id, State.DELETED);
            if (user == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Ce user n'existe pas");
            } else {
                userRepository.delete(user);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimées avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }

}
