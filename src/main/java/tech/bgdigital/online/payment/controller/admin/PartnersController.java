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
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.PartnerRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/partners")
@Api(tags = "Admin partenaire",description = ".")
public class PartnersController {
    final PartnerRepository partnerRepository;

    final
    HttpResponseApiInterface httpResponseApi;
    public PartnersController(PartnerRepository partnerRepository, HttpResponseApiInterface httpResponseApi) {
        this.partnerRepository = partnerRepository;
        this.httpResponseApi = httpResponseApi;
    }
    @GetMapping("")
    @ApiOperation(value = "Liste des partenaires")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(partnerRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }
    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des partenaires paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Partner> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Partner> pageTuts;
            pageTuts = partnerRepository.findAllByStateIsNot(State.DELETED, paging);
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
    @ApiOperation(value = "Ajouter relevé de compte")
    public Map<String, Object> store(@Valid @RequestBody Partner partner) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal('0')) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/

            partnerRepository.save(partner);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(partner, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(partner, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details partenaire paginé")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            Partner partner = partnerRepository.findByIdAndStateNot(id, State.DELETED);
            if (partner != null) {
                return httpResponseApi.response(partner, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cette région n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  partenaires paginés")
    public Map<String, Object> updateRegion(@Valid @RequestBody Partner partner) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal('0')) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
                Partner partner1 = partnerRepository.findByIdAndStateNot(partner.getId(), State.DELETED);
                if (partner1 == null) {
                    return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce relevés de compte n'existe pas.");
                } else {
                    //todo set all value you want to update from accountStatement
                    partner.setState(partner.getState());
                    partnerRepository.save(partner1);
                    return httpResponseApi.response(partner, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
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
    @ApiOperation(value = "Changer etat des relevés de compte paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            Partner partnerExist = partnerRepository.findByIdAndStateNot(id, State.DELETED);
            if ((partnerExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (partnerExist.getState().equals(State.ACTIVED)) {
                partnerExist.setState(State.ACTIVED);
                message = "etat activé avec succéss";

            } else {
                partnerExist.setState(State.DISABLED);
                message = "etat desactivé avec succéss";
            }

            partnerRepository.save(partnerExist);
            return httpResponseApi.response(partnerExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer les relevés de compte paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            Partner partner = partnerRepository.findByIdAndStateNot(id, State.DELETED);
            if (partner == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Ce relevés de compte n'existe pas");
            } else {
                partnerRepository.delete(partner);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimé avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }
}
