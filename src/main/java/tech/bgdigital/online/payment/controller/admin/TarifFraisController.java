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
import tech.bgdigital.online.payment.models.entity.Profil;
import tech.bgdigital.online.payment.models.entity.TarifFrai;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.TarifFraiRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/tarif-frais")
@Api(tags = "Admin tarif frais",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TarifFraisController {
    final TarifFraiRepository tarifFraiRepository;
    final
    HttpResponseApiInterface httpResponseApi;

    public TarifFraisController(TarifFraiRepository tarifFraiRepository, HttpResponseApiInterface httpResponseApi) {
        this.tarifFraiRepository = tarifFraiRepository;
        this.httpResponseApi = httpResponseApi;
    }


    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des tarifFrais paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<TarifFrai> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<TarifFrai> pageTuts;
            pageTuts = tarifFraiRepository.findAllByStateIsNot(State.DELETED, paging);
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
    @ApiOperation(value = "Liste des TarifFrais")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(tarifFraiRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }


    @PostMapping("")
    @ApiOperation(value = "Ajouter tariFrais")
    public Map<String, Object> store(@Valid @RequestBody TarifFrai tarifFrai) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/
            tarifFrai.setState("ACTIVED");
            tarifFraiRepository.save(tarifFrai);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(tarifFrai, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(tarifFrai, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  tarifFrais paginés")
    public Map<String, Object> updateRegion(@Valid @RequestBody TarifFrai tarifFrai) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
            TarifFrai tarifFrai1 = tarifFraiRepository.findByIdAndStateNot(tarifFrai.getId(), State.DELETED);
            if (tarifFrai1 == null) {
                return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce tarifFrais n'existe pas.");
            } else {
                //todo set all value you want to update from accountStatement
                tarifFrai.setState(tarifFrai.getState());
                tarifFraiRepository.save(tarifFrai1);
                return httpResponseApi.response(tarifFrai, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
            }
            //}

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }


    @GetMapping("{id}")
    @ApiOperation(value = "Voir details tarifFrais paginés")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            TarifFrai tarifFrai = tarifFraiRepository.findByIdAndStateNot(id, State.DELETED);
            if (tarifFrai != null) {
                return httpResponseApi.response(tarifFrai, HttpStatus.CREATED.value(), false, "Donnée disponible.");
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
    @ApiOperation(value = "Changer etat des tarifFrais paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            TarifFrai tarifFraiExist = tarifFraiRepository.findByIdAndStateNot(id, State.DELETED);
            if ((tarifFraiExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (tarifFraiExist.getState().equals(State.ACTIVED)) {
                tarifFraiExist.setState(State.DISABLED);
                message = "etat activé avec succéss";

            } else {
                tarifFraiExist.setState(State.ACTIVED);
                message = "etat desactivé avec succéss";
            }

            tarifFraiRepository.save(tarifFraiExist);
            return httpResponseApi.response(tarifFraiExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer les tarifFrais paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            TarifFrai tarifFrai = tarifFraiRepository.findByIdAndStateNot(id, State.DELETED);
            if (tarifFrai == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Ce tarifFrais n'existe pas");
            } else {
                tarifFraiRepository.delete(tarifFrai);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimées avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }
}
