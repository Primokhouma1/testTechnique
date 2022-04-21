package tech.bgdigital.online.payment.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.exceptions.NotDeleteEntityException;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Profil;
import tech.bgdigital.online.payment.models.entity.User;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.ProfilRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/profil")
@Api(tags = "Admin profil",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)

public class ProfileController {
    final ProfilRepository profilRepository;
    final
    HttpResponseApiInterface httpResponseApi;

    public ProfileController(ProfilRepository profilRepository, HttpResponseApiInterface httpResponseApi) {
        this.profilRepository = profilRepository;
        this.httpResponseApi = httpResponseApi;
    }


    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des Profils paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Profil> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Profil> pageTuts;
            pageTuts = profilRepository.findAllByStateIsNot(State.DELETED, paging);
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
    @ApiOperation(value = "Liste des Profils")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(profilRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }

    @PostMapping("")
    @ApiOperation(value = "Ajouter profil")
    public Map<String, Object> store(@Valid @RequestBody Profil profil) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/
            profil.setState("ACTIVED");
            profilRepository.save(profil);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(profil, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(profil, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  profil paginé")
    public Map<String, Object> updateRegion(@Valid @RequestBody Profil profil) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
            Profil profil1 = profilRepository.findByIdAndStateNot(profil.getId(), State.DELETED);
            if (profil1 == null) {
                return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce profil n'existe pas.");
            } else {
                //todo set all value you want to update from accountStatement
                profil.setState(profil.getState());
                profilRepository.save(profil1);
                return httpResponseApi.response(profil, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
            }
            //}

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details profil paginé")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            Profil profil = profilRepository.findByIdAndStateNot(id, State.DELETED);
            if (profil != null) {
                return httpResponseApi.response(profil, HttpStatus.CREATED.value(), false, "Donnée disponible.");
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
    @ApiOperation(value = "Changer etat des profils paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            Profil profilExist = profilRepository.findByIdAndStateNot(id, State.DELETED);
            if ((profilExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (profilExist.getState().equals(State.ACTIVED)) {
                profilExist.setState(State.DISABLED);
                message = "etat activé avec succéss";

            } else {
                profilExist.setState(State.ACTIVED);
                message = "etat desactivé avec succéss";
            }

            profilRepository.save(profilExist);
            return httpResponseApi.response(profilExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer profil paginé")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            Profil profil = profilRepository.findByIdAndStateNot(id, State.DELETED);
            if (profil == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Ce profil n'existe pas");
            } else {
                profilRepository.delete(profil);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimé avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }


    @GetMapping("/search")
    @ApiOperation(value = "Voir details profil recherché")
    public Map<String, Object> showRecherche(
            @RequestParam(required = false) String name) {
        try {

            List<Profil> profil =  profilRepository.findProfilsByName(name);
            if (profil != null) {
                return httpResponseApi.response(profil, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cet profil n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }
}
