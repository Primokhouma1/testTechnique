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
import tech.bgdigital.online.payment.models.entity.CallFund;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.CallFundRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/call-fund")
@Api(tags = "Admin appel de fonds",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CallFundController {
    final CallFundRepository callFundRepository;

    final
    HttpResponseApiInterface httpResponseApi;

    public CallFundController(CallFundRepository callFundRepository, HttpResponseApiInterface httpResponseApi) {
        this.callFundRepository = callFundRepository;
        this.httpResponseApi = httpResponseApi;
    }

    @GetMapping("")
    @ApiOperation(value = "Liste des appels de fonds")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(callFundRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }
    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des appels de fonds paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<CallFund> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<CallFund> pageTuts;
            pageTuts = callFundRepository.findAllByStateIsNot(State.DELETED, paging);
            listType = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response = httpResponseApi.paginate(listType, pageTuts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            return new ResponseEntity<>(httpResponseApi.response(null, 500, true,"Ok"), HttpStatus.OK);
        }
    }

    @PostMapping("")
    @ApiOperation(value = "Ajouter appel de fonds")
    public Map<String, Object> store(@Valid @RequestBody CallFund callFund) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/
            callFund.setState("ACTIVED");
            callFundRepository.save(callFund);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(callFund, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(callFund, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details appels de fonds paginé")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            CallFund callFund = callFundRepository.findByIdAndStateNot(id, State.DELETED);
            if (callFund != null) {
                return httpResponseApi.response(callFund, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cet appel de fonds n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  appel de fonds paginés")
    public Map<String, Object> updateRegion(@Valid @RequestBody CallFund callFund) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
            CallFund callFund1 = callFundRepository.findByIdAndStateNot(callFund.getId(), State.DELETED);
            if (callFund1 == null) {
                return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce appel de fonds n'existe pas.");
            } else {
                //todo set all value you want to update from accountStatement
                callFund.setState(callFund.getState());
                callFundRepository.save(callFund1);
                return httpResponseApi.response(callFund, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
            }
            //}

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }

    /**
     * Desactiver etat a 0
     */
    @GetMapping("/state/{id}")
    @Transactional
    @ApiOperation(value = "Changer etat des appels de fonds paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            CallFund callFundExist = callFundRepository.findByIdAndStateNot(id, State.DELETED);
            if ((callFundExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (callFundExist.getState().equals(State.ACTIVED)) {
                callFundExist.setState(State.ACTIVED);
                message = "etat activé avec succéss";

            } else {
                callFundExist.setState(State.DISABLED);
                message = "etat desactivé avec succéss";
            }

            callFundRepository.save(callFundExist);
            return httpResponseApi.response(callFundExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer appel de fonds paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            CallFund callFund = callFundRepository.findByIdAndStateNot(id, State.DELETED);
            if (callFund == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Cet appel de fonds n'existe pas");
            } else {
                callFundRepository.delete(callFund);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimé avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }
}
